package com.swd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.exception.NotSupportedException;
import com.swd.exception.ResourceNotFoundException;
import com.swd.exception.UniqueConstraintException;
import com.swd.model.dto.UserDTO;
import com.swd.model.entity.Account;
import com.swd.model.entity.User;
import com.swd.model.entity.Store;
import com.swd.repository.AccountRepository;
import com.swd.repository.UserRepository;
import com.swd.repository.StoreRepository;
import com.swd.service.HelperService;
import com.swd.service.UserService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, HelperService<User, UserDTO>, UserDetailsService {
    private static final String MANAGER_NOT_FOUND = "Can't find User with id: %s ";
    private static final String STORE_NOT_FOUND = "Can't find Store with id %s";
    private static final String UNIQUE_CONSTRAINT = "%s is already taken";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserDTO add(UserDTO userDTO) {
        boolean checkUsername = accountRepository.existsByUsername(userDTO.getAccount().getUsername());
        if (checkUsername) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getAccount().getUsername()));
        }
        boolean checkEmail = userRepository.existsByEmail(userDTO.getEmail());
        if (checkEmail) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getEmail()));
        }
        boolean checkPhone = userRepository.existsByPhone(userDTO.getPhone());
        if (checkPhone) {
            throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getPhone()));
        }
        User user = convertDTOToEntity(userDTO);
        Account account = objectMapper.convertValue(userDTO.getAccount(), Account.class);
        account.setUser(user);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setEnabled(true);
        user.setAccount(account);
        return convertEntityToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO find(long id, List<String> fields) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent() && fields != null) {
            return convertEntityToFilteredDTO(optionalUser.get(), fields);
        }
        return optionalUser.map(this::convertEntityToDTO).orElseThrow(() ->
                new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id)));
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable, List<String> fields) {
        Page<User> userPage = userRepository.findAll(pageable);
        if (userPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return userPage.map(user -> convertEntityToFilteredDTO(user, fields));
        }
        return userPage.map(this::convertEntityToDTO);
    }

    @Override
    public Page<UserDTO> findAllWithSearch(Pageable pageable,
                                           List<String> fields,
                                           String searchBy,
                                           String searchValue) {
        Page<User> userPage;
        switch (searchBy) {
            case "name":
                userPage = userRepository.findByNameContaining(searchValue, pageable);
                break;
            case "phone":
                userPage = userRepository.findByPhoneContaining(searchValue, pageable);
                break;
            case "email":
                userPage = userRepository.findByEmailContaining(searchValue, pageable);
                break;
            case "store_id":
                userPage = userRepository.findByStoreId(Long.parseLong(searchValue), pageable);
                break;
            default:
                throw new NotSupportedException("System doesn't support search by " + searchBy);
        }
        if (userPage.isEmpty()) {
            return Page.empty();
        }
        if (fields != null) {
            return userPage.map(user -> convertEntityToFilteredDTO(user, fields));
        }
        return userPage.map(this::convertEntityToDTO);
    }

    @Override
    public void updateUserStore(Long userId, Long storeId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, userId));
        }
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (!optionalStore.isPresent()) {
            throw new ResourceNotFoundException(String.format(STORE_NOT_FOUND, userId));
        }
        User user = optionalUser.get();
        user.setStore(optionalStore.get());
        userRepository.save(user);
    }

    @Override
    public UserDTO update(long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user ->
                convertEntityToDTO(userRepository.save(updateFields(user, userDTO))))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id)));
    }

    @Override
    public void delete(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new ResourceNotFoundException(String.format(MANAGER_NOT_FOUND, id));
        }
    }

    @Override
    public User updateFields(User user, UserDTO userDTO) {
        if (!userDTO.getAccount().getUsername().equals(user.getAccount().getUsername())) {
            boolean checkUsername = accountRepository.existsByUsername(userDTO.getAccount().getUsername());
            if (checkUsername) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getAccount().getUsername()));
            }
            user.getAccount().setUsername(userDTO.getAccount().getUsername());
        }
        if (!userDTO.getEmail().equals(user.getEmail())) {
            boolean checkEmail = userRepository.existsByEmail(userDTO.getEmail());
            if (checkEmail) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getEmail()));
            }
            user.setEmail(userDTO.getEmail());
        }
        if (!userDTO.getPhone().equals(user.getPhone())) {
            boolean checkPhone = userRepository.existsByPhone(userDTO.getPhone());
            if (checkPhone) {
                throw new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, userDTO.getPhone()));
            }
            user.setPhone(userDTO.getPhone());
        }
        user.setName(userDTO.getName());
        user.getAccount().setPassword(passwordEncoder.encode(userDTO.getAccount().getPassword()));
        user.getAccount().setEnabled(userDTO.getAccount().isEnabled());
        return user;
    }

    @Override
    public User convertDTOToEntity(UserDTO userDTO) {
        return objectMapper.convertValue(userDTO, User.class);
    }

    @Override
    public UserDTO convertEntityToDTO(User user) {
        return objectMapper.convertValue(user, UserDTO.class);
    }

    @Override
    public UserDTO convertEntityToFilteredDTO(User user, List<String> fields) {
        return convertEntityToDTO(filterFields(user, fields));
    }

    @Override
    public User filterFields(User user, List<String> fields) {
        User filteredUser = new User();
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(user);
        List<String> unwantedFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (!fields.contains(propertyDescriptor.getName())) {
                unwantedFields.add(propertyDescriptor.getName());
            }
        }
        BeanUtils.copyProperties(user, filteredUser, unwantedFields.toArray(new String[0]));
        return filteredUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                optionalAccount.get().getUsername(),
                optionalAccount.get().getPassword(),
                new ArrayList<>());
    }
}
