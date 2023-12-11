package org.project2.library.controllers;

import org.project2.library.models.Book;
import org.project2.library.models.Person;
import org.project2.library.services.BooksService;
import org.project2.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BookController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "wordInput", required = false) String wordInput, Model model) {
        if (wordInput != null) {
            model.addAttribute("book", booksService.findByNameStartingWith(wordInput));
        }
        return "books/search";
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer books_per_page,
                        @RequestParam(value = "sort_by_year", defaultValue = "false", required = false) boolean sort_by_year) {

        if (page != null && books_per_page != null) {
            if (sort_by_year) {
                model.addAttribute("books", booksService.findAll(page, books_per_page, sort_by_year));
            } else {
                model.addAttribute("books", booksService.findAll(page, books_per_page));
            }
        } else {
            if (sort_by_year) {
                model.addAttribute("books", booksService.findAll(sort_by_year));
            } else {
                model.addAttribute("books", booksService.findAll());
            }
        }
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "books/new";
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));

        model.addAttribute("personWithBook", booksService.join(id));

        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("person", new Person());

        return "books/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set")
    public String set(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.set(id, person);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/clear")
    public String clear(@PathVariable("id") int id) {
        booksService.clear(id);
        return "redirect:/books/{id}";
    }
}
