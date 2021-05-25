package br.com.jusnexo.service.impl;

import br.com.jusnexo.domain.AreaOfExpertise;
import br.com.jusnexo.repository.AreaOfExpertiseRepository;
import br.com.jusnexo.service.AreaOfExpertiseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AreaOfExpertise}.
 */
@Service
@Transactional
public class AreaOfExpertiseServiceImpl implements AreaOfExpertiseService {

    private final Logger log = LoggerFactory.getLogger(AreaOfExpertiseServiceImpl.class);

    private final AreaOfExpertiseRepository areaOfExpertiseRepository;

    public AreaOfExpertiseServiceImpl(AreaOfExpertiseRepository areaOfExpertiseRepository) {
        this.areaOfExpertiseRepository = areaOfExpertiseRepository;
    }

    @Override
    public AreaOfExpertise save(AreaOfExpertise areaOfExpertise) {
        log.debug("Request to save AreaOfExpertise : {}", areaOfExpertise);
        return areaOfExpertiseRepository.save(areaOfExpertise);
    }

    @Override
    public Optional<AreaOfExpertise> partialUpdate(AreaOfExpertise areaOfExpertise) {
        log.debug("Request to partially update AreaOfExpertise : {}", areaOfExpertise);

        return areaOfExpertiseRepository
            .findById(areaOfExpertise.getId())
            .map(
                existingAreaOfExpertise -> {
                    if (areaOfExpertise.getAreaName() != null) {
                        existingAreaOfExpertise.setAreaName(areaOfExpertise.getAreaName());
                    }
                    if (areaOfExpertise.getIsSelected() != null) {
                        existingAreaOfExpertise.setIsSelected(areaOfExpertise.getIsSelected());
                    }

                    return existingAreaOfExpertise;
                }
            )
            .map(areaOfExpertiseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaOfExpertise> findAll(Pageable pageable) {
        log.debug("Request to get all AreaOfExpertises");
        return areaOfExpertiseRepository.findAll(pageable);
    }

    public Page<AreaOfExpertise> findAllWithEagerRelationships(Pageable pageable) {
        return areaOfExpertiseRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaOfExpertise> findOne(Long id) {
        log.debug("Request to get AreaOfExpertise : {}", id);
        return areaOfExpertiseRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AreaOfExpertise : {}", id);
        areaOfExpertiseRepository.deleteById(id);
    }
}
