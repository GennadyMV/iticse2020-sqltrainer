package rage.sqltrainer.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rage.sqltrainer.domain.Topic;
import rage.sqltrainer.repository.TopicRepository;

@Controller
@Secured("ROLE_ADMIN")
public class TopicController {

    @Autowired
    TopicRepository topicRepository;

    @GetMapping("/topics")
    String getAll(Model model) {
        model.addAttribute("topics", topicRepository.findAll(Sort.by("rank")));
        return "topics/list";
    }

    @GetMapping("/topics/add")
    String viewForm(@ModelAttribute Topic topic) {
        return "topics/add";
    }

    @GetMapping("/topics/{id}")
    String getOne(Model model, @PathVariable Long id) {
        Topic topic = topicRepository.getOne(id);
        model.addAttribute("topic", topic);

        return "topics/view";
    }

    @PostMapping("/topics")
    String create(@Valid @ModelAttribute Topic topic, BindingResult res) {
        if (res.hasErrors()) {
            return viewForm(topic);
        }

        topicRepository.save(topic);
        return "redirect:/topics";
    }
}
