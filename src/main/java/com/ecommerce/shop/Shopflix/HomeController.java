package com.ecommerce.shop.Shopflix;

import com.ecommerce.shop.Shopflix.entity.User;
import com.ecommerce.shop.Shopflix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@RequestMapping("/shopflix")
@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private JavaMailSender javaMailSender;
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView home(HttpSession session) {
        User user = (User) session.getAttribute("LOGGED_IN");
        if (user == null) {
            logger.info("user not logged in");
        } else {
            logger.info("user logged in");
            return new ModelAndView("index", "user", user);
        }
        return new ModelAndView("index", "user", new User());
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        User u = new User();
        return new ModelAndView("login","user",u);
    }

    @PostMapping("/loginuser")
    public void signInUserWithUsernameAndPassword(@ModelAttribute User user, HttpSession session, HttpServletResponse response) throws IOException {
        Optional<User> details = userRepository.findById(user.getEmail());
        details.ifPresent(user1 -> {
            System.out.println(user1.getEmail());
            session.setAttribute("LOGGED_IN", user1);
            logger.info("Here user logged in Session started");
            try {
                response.sendRedirect("/shopflix/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/registeruser")
    public void registerUser(@ModelAttribute User User, HttpServletResponse response) {
        try {
            Optional<User> user = userRepository.findById(User.getEmail());
            boolean alreadyExists = user.isPresent();

            if (alreadyExists) {
                logger.info("Already Registered");
                response.sendRedirect("/shopflix/login");
            } else {
             //   sendMail(User.getEmail(), User.getPassword());
                userRepository.save(User);
               response.sendRedirect("/shopflix/login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void sendMail(String email, String password) {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(email);
//        simpleMailMessage.setSubject("Welcome to Shopflix");
//        simpleMailMessage.setText("Your Registration for Shopflix  id Successful\n Your Password is " + password);
//        javaMailSender.send(simpleMailMessage);
//    }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response, HttpSession session) throws IOException {
        session.invalidate();
        response.sendRedirect("/shopflix/home");
    }
    public ModelAndView alreadyRegistered(User user) {
        return new ModelAndView("login","user",user);
    }
}
