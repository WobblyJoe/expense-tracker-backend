
package com.example.expense.controller;

import com.example.expense.model.Expense;
import com.example.expense.repository.ExpenseRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseRepository repository;

    public ExpenseController(ExpenseRepository repository) {
        this.repository = repository;
    }

    // GET - Display all expenses
    @GetMapping
    public List<Expense> getAllExpenses(
            @RequestParam(required = false) String category) {

        if (category != null && !category.isBlank()) {
            return repository.findByCategoryIgnoreCase(category);
        }

        return repository.findAll();
    }

    // POST - Add expense
    @PostMapping
    public Expense addExpense(@Valid @RequestBody Expense expense) {
        return repository.save(expense);
    }

    // PUT - Update expense
    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense) {

        Expense existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Expense not found"));

        existing.setTitle(expense.getTitle());
        existing.setAmount(expense.getAmount());
        existing.setCategory(expense.getCategory());
        existing.setDate(expense.getDate());

        return repository.save(existing);
    }

    // DELETE - Delete expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Expense not found");
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
