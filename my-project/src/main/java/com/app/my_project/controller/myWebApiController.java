package com.app.my_project.controller;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.app.my_project.annotation.RequireAuth;
import com.app.my_project.entity.myWebEntity;
import com.app.my_project.repository.myWebRepository;

@RestController
@RequestMapping("/myWeb")
public class myWebApiController {
    @Autowired
    private myWebRepository mywebRepository;

    // @GetMapping
    // // @RequireAuth
    // public List<myWebEntity> read() {
    //     return myWebRepository.findAllByOrderByIdAsc();
    // }

    public record PageSettings(
            String searchBy,
            String keyword,
            Integer page,
            Integer size,
            Sort.Direction direction,
            String sortBy) {

        public static final Set<String> ALLOWED_KEYS = Set.of(
                "name", "region", "element", "gender", "rarity");

        public PageSettings {

            // Default to Page 0 if missing or negative
            if (page == null || page < 0)
                page = 0;

            // Default to Size 10 if missing or invalid
            if (size == null || size < 1)
                size = 10;

            // Default to Ascending if missing
            if (direction == null)
                direction = Sort.Direction.ASC;

            // Default to sorting by "id" if missing or invalid
            if (sortBy == null || !ALLOWED_KEYS.contains(sortBy))
                sortBy = "id";

            // Default to searching by "id" if not exactly available in DB columns
            if (searchBy == null || !ALLOWED_KEYS.contains(searchBy))
                searchBy = "id";

            // Prevent Null-Pointer-Exception
            if (keyword == null)
                keyword = "";

            // When searching with Rarity, Default to keyword === number
            String regex = "[+-]?\\d+(\\.\\d+)?";
            if (searchBy.equals("rarity") && !Pattern.matches(regex, keyword))
                keyword = "0";
        }
    }

    @GetMapping
    public Page<myWebEntity> read(@RequestBody PageSettings req) {

        // Slice<myWebEntity> r =
        // mywebRepository.findAll(PageRequest.of(0, 10));

        Pageable pageable = PageRequest.of(req.page, req.size, req.direction,
                req.sortBy);
        return switch (req.searchBy) {
            case "name" -> mywebRepository.findByNameContaining(req.keyword,
                    pageable);
            case "region" -> mywebRepository.findByRegionContaining(req.keyword,
                    pageable);
            case "element" ->
                mywebRepository.findByElementContaining(req.keyword, pageable);
            case "gender" -> mywebRepository.findByGenderContaining(req.keyword,
                    pageable);
            case "rarity" ->
                mywebRepository.findByRarityEquals(Integer.valueOf(req.keyword),
                        pageable);

            // Always have a default in case an unknown string comes in
            default -> mywebRepository.findAll(pageable);
        };

    }

    @PostMapping
    public ResponseEntity<myWebEntity> create(
            @RequestBody myWebEntity myWebEntity) {
        return ResponseEntity.ok(mywebRepository.save(myWebEntity));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody List<myWebEntity> req) {
        for (myWebEntity obj : req) {
            myWebEntity item = mywebRepository.findById(obj.getId()).orElse(null);
            item.setName(obj.getName());
            item.setRegion(obj.getRegion());
            item.setElement(obj.getElement());
            item.setGender(obj.getGender());
            item.setRarity(obj.getRarity());
            mywebRepository.save(item);
        }

        if (req == null) {
            throw new IllegalArgumentException("pp not found");
        }

        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        mywebRepository.deleteById(id);
    }

}
