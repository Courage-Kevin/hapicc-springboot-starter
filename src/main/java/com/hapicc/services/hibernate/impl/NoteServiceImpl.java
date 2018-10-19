package com.hapicc.services.hibernate.impl;

import com.hapicc.pojo.JqGridResult;
import com.hapicc.pojo.hibernate.Note;
import com.hapicc.repository.NoteRepository;
import com.hapicc.services.hibernate.NoteService;
import com.hapicc.utils.common.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    @Transactional
    public Note save(Note note) throws Exception {
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note update(Long id, Note note) {
        note.setId(id);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public Note get(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    @Override
    public JqGridResult list(Map<String, String> params) throws Exception {

        Integer page = RequestUtils.getPage(params, true);
        Integer rows = RequestUtils.getRows(params);
        String sidx = RequestUtils.getSidx(params, true);
        String sord = RequestUtils.getSord(params);
        boolean needTotal = RequestUtils.needTotal(params);
        String q = RequestUtils.getQ(params, true);

        Pageable pageable = PageRequest.of(page, rows, Sort.Direction.fromString(sord), sidx);

        Page<Note> notePage;
        if (q == null) {
            notePage = noteRepository.findAll(pageable);
        } else {
            notePage = noteRepository.findAll((Specification<Note>) (root, query, criteriaBuilder) -> {
                Predicate p1 = criteriaBuilder.like(root.get("title").as(String.class), q, RequestUtils.ESCAPE);
                Predicate p2 = criteriaBuilder.like(root.get("content").as(String.class), q, RequestUtils.ESCAPE);
                return criteriaBuilder.or(p1, p2);
            }, pageable);
        }

        return new JqGridResult(notePage, needTotal);
    }

    @Override
    public Note getNoteSimpleInfo(Long id) {
        return noteRepository.findNoteSimpleInfo(id);
    }
}
