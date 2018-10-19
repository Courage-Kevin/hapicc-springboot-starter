package com.hapicc.controllers.hibernate;

import com.hapicc.pojo.HapiccJSONResult;
import com.hapicc.pojo.hibernate.Note;
import com.hapicc.services.hibernate.NoteService;
import com.hapicc.utils.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("hibernate")
public class HibernateCRUDController {

    @Autowired
    private NoteService noteService;

    @PostMapping("note")
    public HapiccJSONResult save(@Valid @RequestBody Note note) {
        try {
            return HapiccJSONResult.ok(noteService.save(note));
        } catch (Exception e) {
            log.warn("Error occurred when save note: " + JsonUtils.obj2Json(note), e);
            return HapiccJSONResult.errorException("Error occurred when save note!");
        }
    }

    @PutMapping("note/{id}")
    public HapiccJSONResult update(@PathVariable("id") Long noteId, @Valid @RequestBody Note noteDetails) {
        Note note = noteService.get(noteId);
        if (note == null) {
            return HapiccJSONResult.build(404, "The note not found!");
        }

        try {
            BeanUtils.copyProperties(noteDetails, note);
            return HapiccJSONResult.ok(noteService.update(noteId, note));
        } catch (Exception e) {
            log.warn("Error occurred when update note, noteId: " + noteId + ", noteDetails: " + JsonUtils.obj2Json(noteDetails), e);
            return HapiccJSONResult.errorException("Error occurred when update note!");
        }
    }

    @DeleteMapping("note/{id}")
    public HapiccJSONResult delete(@PathVariable("id") Long noteId) {

        if (noteService.get(noteId) == null) {
            return HapiccJSONResult.build(404, "The note not found!");
        }

        noteService.delete(noteId);
        return HapiccJSONResult.ok();
    }

    @GetMapping("note/{id}")
    public HapiccJSONResult get(@PathVariable("id") Long noteId) {

        Note note = noteService.get(noteId);

        if (note != null) {
            return HapiccJSONResult.ok(note);
        } else {
            return HapiccJSONResult.build(404, "The note not found!");
        }
    }

    @GetMapping("note")
    public HapiccJSONResult list(@RequestParam Map<String, String> params) {
        try {
            return HapiccJSONResult.ok(noteService.list(params));
        } catch (Exception e) {
            log.warn("Error occurred when list note with params: " + JsonUtils.obj2Json(params), e);
            return HapiccJSONResult.build(400, "Invalid parameters!");
        }
    }

    @GetMapping("note/{id}/simple")
    public HapiccJSONResult getNoteSimpleInfo(@PathVariable("id") Long noteId) {

        Note note = noteService.getNoteSimpleInfo(noteId);

        if (note != null) {
            return HapiccJSONResult.ok(note);
        } else {
            return HapiccJSONResult.build(404, "The note not found!");
        }
    }
}
