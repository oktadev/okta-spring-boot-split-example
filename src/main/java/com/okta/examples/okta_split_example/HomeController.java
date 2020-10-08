package com.okta.examples.okta_split_example;

import io.split.client.SplitClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    SplitClient splitClient;

    public HomeController(SplitClient splitClient) {
        this.splitClient = splitClient;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OidcUser user, Model model) {
        model.addAttribute("username", user.getPreferredUsername());
        model.addAttribute("roles", user.getAuthorities());

        List<String> groups =  user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String inBeta = splitClient.getTreatment(
            user.getPreferredUsername(),
            "BETA_UI_EXPERIENCE",
            Map.of("groups", groups)
        );

        return "on".equals(inBeta) ? "home-beta" : "home";
    }
}