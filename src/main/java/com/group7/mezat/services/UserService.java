package com.group7.mezat.services;

import com.group7.mezat.documents.Role;
import com.group7.mezat.responses.ErrorResponse;
import com.group7.mezat.responses.UserResponse;
import com.group7.mezat.documents.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.group7.mezat.repos.UserRepository;
import com.group7.mezat.requests.UserPasswordUpdateRequest;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public UserResponse getOneUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        return new UserResponse(user);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    public ResponseEntity<ErrorResponse> updateUserPassword(String id, UserPasswordUpdateRequest newUser) {
        Optional<User> user = userRepository.findById(id);
        ErrorResponse response = new ErrorResponse();
        if (user.isPresent()){
            User foundUser = user.get();
            System.out.println(foundUser.getPassword());
            if (foundUser.getPassword().equals(newUser.getOldPassword())){
                foundUser.setPassword(newUser.getPassword());
                userRepository.save(foundUser);
                return new ResponseEntity<>(response, HttpStatus.OK); //ok
            }
            else{
                response.setMessage("Girdiğiniz şifre hatalı!");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //bad request
            }

        }else{
            response.setMessage("Oturumun süresi doldu!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //bad request
        }
    }

    public void saveUser(User user) {
        userRepository.insert(user);
    }

    public UserResponse getOneUserByEmail(String Email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserMail(Email));
        if (user.isPresent()){
            User found = user.get();
            return new UserResponse(found);
        }
        return null;
    }

    public User addCooperativeRoleToUser(String userName) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserMail(userName));
        if (user.isPresent()){
            User foundUser = user.get();
//            if (foundUser.getRole() == Role.ADMIN){
////! exception
//                return null;
//            }
//            foundUser.setRole(Role.COOPERATIVE);
            return userRepository.save(foundUser);
        }else{
            //! exception
            return null;
        }
    }

    public User getOneUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User found = user.get();
            return found;
        }
        return null;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
