package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"/", "index"})
    public String index(Model model) {
        model.addAttribute("users", repository.findAll());
        return "index";
    }

    @GetMapping("/signup")
    public String addNewUser(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user,
                          BindingResult result,
                          Model model) {
        log.info("User object received: {}", user);

        if(result.hasErrors()) {
            return "add-user";
        }

        User savedUser = repository.save(user);
        log.info("Created new user: {}", savedUser);

        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        log.info("Editing user with id: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid User user,
                             BindingResult result,
                             @PathVariable("id") Long id,
                             Model model) {
        log.info("Updating user: {}", user);
        if(result.hasErrors()) {
            return "update-user";
        }

        User saved = repository.save(user);

        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        log.info("Deleting user with id: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invlid user id: " + id));

        repository.delete(user);
        return "redirect:/index";
    }
}
