package com.planeta.pfum.web.rest;

import com.planeta.pfum.domain.Annonce;
import com.planeta.pfum.repository.AnnonceRepository;
import com.planeta.pfum.repository.search.AnnonceSearchRepository;
import com.planeta.pfum.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.planeta.pfum.domain.Annonce}.
 */
@RestController
@RequestMapping("/api")
public class AnnonceResource {

    private final Logger log = LoggerFactory.getLogger(AnnonceResource.class);

    private static final String ENTITY_NAME = "annonce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnonceRepository annonceRepository;

    private final AnnonceSearchRepository annonceSearchRepository;

    public AnnonceResource(AnnonceRepository annonceRepository, AnnonceSearchRepository annonceSearchRepository) {
        this.annonceRepository = annonceRepository;
        this.annonceSearchRepository = annonceSearchRepository;
    }

    /**
     * {@code POST  /annonces} : Create a new annonce.
     *
     * @param annonce the annonce to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annonce, or with status {@code 400 (Bad Request)} if the annonce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annonces")
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) throws URISyntaxException {
        log.debug("REST request to save Annonce : {}", annonce);
        if (annonce.getId() != null) {
            throw new BadRequestAlertException("A new annonce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annonce result = annonceRepository.save(annonce);
        annonceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/annonces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annonces} : Updates an existing annonce.
     *
     * @param annonce the annonce to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonce,
     * or with status {@code 400 (Bad Request)} if the annonce is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annonce couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annonces")
    public ResponseEntity<Annonce> updateAnnonce(@RequestBody Annonce annonce) throws URISyntaxException {
        log.debug("REST request to update Annonce : {}", annonce);
        if (annonce.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Annonce result = annonceRepository.save(annonce);
        annonceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonce.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annonces} : get all the annonces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annonces in body.
     */
    @GetMapping("/annonces")
    public List<Annonce> getAllAnnonces() {
        log.debug("REST request to get all Annonces");
        return annonceRepository.findAll();
    }

    /**
     * {@code GET  /annonces/:id} : get the "id" annonce.
     *
     * @param id the id of the annonce to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annonce, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annonces/{id}")
    public ResponseEntity<Annonce> getAnnonce(@PathVariable Long id) {
        log.debug("REST request to get Annonce : {}", id);
        Optional<Annonce> annonce = annonceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(annonce);
    }

    /**
     * {@code DELETE  /annonces/:id} : delete the "id" annonce.
     *
     * @param id the id of the annonce to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annonces/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        log.debug("REST request to delete Annonce : {}", id);
        annonceRepository.deleteById(id);
        annonceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/annonces?query=:query} : search for the annonce corresponding
     * to the query.
     *
     * @param query the query of the annonce search.
     * @return the result of the search.
     */
    @GetMapping("/_search/annonces")
    public List<Annonce> searchAnnonces(@RequestParam String query) {
        log.debug("REST request to search Annonces for query {}", query);
        return StreamSupport
            .stream(annonceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
