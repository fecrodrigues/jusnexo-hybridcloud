package br.com.jusnexo.service.impl;

import br.com.jusnexo.domain.ClientRating;
import br.com.jusnexo.repository.ClientRatingRepository;
import br.com.jusnexo.service.ClientRatingService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClientRating}.
 */
@Service
@Transactional
public class ClientRatingServiceImpl implements ClientRatingService {

    private final Logger log = LoggerFactory.getLogger(ClientRatingServiceImpl.class);

    private final ClientRatingRepository clientRatingRepository;

    public ClientRatingServiceImpl(ClientRatingRepository clientRatingRepository) {
        this.clientRatingRepository = clientRatingRepository;
    }

    @Override
    public ClientRating save(ClientRating clientRating) {
        log.debug("Request to save ClientRating : {}", clientRating);
        return clientRatingRepository.save(clientRating);
    }

    @Override
    public Optional<ClientRating> partialUpdate(ClientRating clientRating) {
        log.debug("Request to partially update ClientRating : {}", clientRating);

        return clientRatingRepository
            .findById(clientRating.getId())
            .map(
                existingClientRating -> {
                    if (clientRating.getScore() != null) {
                        existingClientRating.setScore(clientRating.getScore());
                    }
                    if (clientRating.getDescription() != null) {
                        existingClientRating.setDescription(clientRating.getDescription());
                    }

                    return existingClientRating;
                }
            )
            .map(clientRatingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientRating> findAll(Pageable pageable) {
        log.debug("Request to get all ClientRatings");
        return clientRatingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientRating> findOne(Long id) {
        log.debug("Request to get ClientRating : {}", id);
        return clientRatingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClientRating : {}", id);
        clientRatingRepository.deleteById(id);
    }
}
