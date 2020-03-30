package com.ecommerce.shop.Shopflix;

import com.ecommerce.shop.Shopflix.entity.CartProducts;
import com.ecommerce.shop.Shopflix.entity.Product;
import com.ecommerce.shop.Shopflix.entity.User;
import com.ecommerce.shop.Shopflix.repository.CartRepository;
import com.ecommerce.shop.Shopflix.repository.ProductRepository;
import com.ecommerce.shop.Shopflix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
@RestController
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

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
    public ModelAndView login(HttpSession session,HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView("login");
        User user = (User)session.getAttribute("LOGGED_IN");
        if (user==null) {
            User u = new User();
            modelAndView.addObject("user",u);
        }
        else {
             response.sendRedirect("/");
        }
        return modelAndView;
    }

    @PostMapping("/loginuser")
    public void signInUserWithUsernameAndPassword(@ModelAttribute User user, HttpSession session, HttpServletResponse response) throws IOException {
        Optional<User> details = userRepository.findById(user.getEmail());
        details.ifPresent(user1 -> {
            System.out.println(user1.getEmail());
            session.setAttribute("LOGGED_IN", user1);
            logger.info("Here user logged in Session started");
            try {
                response.sendRedirect("/");
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
                response.sendRedirect("/login");
            } else {
                //   sendMail(User.getEmail(), User.getPassword());
                userRepository.save(User);
                response.sendRedirect("/login");
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
        response.sendRedirect("/");
    }

    public ModelAndView alreadyRegistered(User user) {
        return new ModelAndView("login", "user", user);
    }

    @RequestMapping(value = "/add_product")
    public Product addProduct(@ModelAttribute Product product) {
        try {
            Product p = productRepository.save(product);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //      return productRepository.save(product);
        return null;
    }

    @GetMapping("/hello")
    public String hello(HttpSession session, @RequestParam("paramid") String paramid) {
        String s = null;
        User user = (User) session.getAttribute("LOGGED_IN");
        if (user == null) s = "0";
        else {
            s = "1";
            try {
                cartRepository.save(new CartProducts(user.getEmail(), paramid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    @RequestMapping("/products")
    public ModelAndView getMiPhones(HttpSession session, @RequestParam("param") String product_brand) {
        List<Product> productList = null;
        ModelAndView modelAndView = new ModelAndView("products");
        User user = (User) session.getAttribute("LOGGED_IN");
        if (user == null) {
            modelAndView.addObject("user", new User());
        } else
            modelAndView.addObject("user", user);

        try {
            productList = productRepository.findByProductBrand(product_brand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("productsList", productList);
        return modelAndView;
    }

    @RequestMapping("/cart")
    public ModelAndView cart(HttpServletResponse response, HttpSession session) throws IOException {
        int price = 0;
        List<Product> productsList = null;
        ModelAndView modelAndView = new ModelAndView("cart");
        User user = (User) session.getAttribute("LOGGED_IN");
        if (user == null) {
            response.sendRedirect("/login");
        } else {
            productsList = new ArrayList<>();
            List<String> cartProducts = cartRepository.cartProducts(user.getEmail());
            for (String c : cartProducts) {
                Optional<Product> product = productRepository.findById(c);
                productsList.add(product.get());
                price += Integer.parseInt(product.get().getProduct_price());
                logger.info(product.get().getProduct_id());
            }
            modelAndView.addObject("total_price", price);
            Collections.reverse(productsList);
            modelAndView.addObject("productsList",productsList);
        }
        return modelAndView;
    }

    @RequestMapping("/data")
    public ModelAndView data(@RequestParam("param1") String product_id, HttpSession session) {
        boolean b = cartRepository.existsById(product_id);
        User user = (User) session.getAttribute("LOGGED_IN");
        ModelAndView modelAndView = new ModelAndView("single_product_page");
        if (user == null) {
            modelAndView.addObject("user", new User());
        } else
            modelAndView.addObject("user", user);
        Optional<Product> optionalProduct = productRepository.findById(product_id);
        boolean isItemAvailable = optionalProduct.isPresent();
        if (isItemAvailable) {
            if (b) {
                modelAndView.addObject("status", "1");
            } else {
                modelAndView.addObject("status", "0");
            }
            modelAndView.addObject("product", optionalProduct.get());
        }
        return modelAndView;
    }

    @RequestMapping("remove")
    public ModelAndView removeFromCart(HttpSession session, HttpServletResponse response, @RequestParam("param1") String prodcut_id) throws IOException {
        cartRepository.deleteById(prodcut_id);
        int price = 0;
        ModelAndView modelAndView = new ModelAndView("cart");
        User user = (User) session.getAttribute("LOGGED_IN");
        if (user == null) {
            response.sendRedirect("/login");
        } else {
            List<Product> productsList = new ArrayList<>();
            List<String> cartProducts = cartRepository.cartProducts(user.getEmail());
            for (String c : cartProducts) {
                Optional<Product> product = productRepository.findById(c);
                productsList.add(product.get());
                price += Integer.parseInt(product.get().getProduct_price());
                logger.info(product.get().getProduct_id());
            }
            Collections.reverse(productsList);
            modelAndView.addObject("total_price", price);
            modelAndView.addObject("productsList", productsList);
        }
        return modelAndView;
    }
}