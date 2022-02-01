package jrbj.training.cookbook.recipe.data;

import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Recipe repository connecting application to Recipe objects in database.
 *
 * All mutation API operations are not exported, leaving only query operations exposed.
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "recipes", path = "recipes")
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {

    /**
     * Fetch RecipeEntity by its ID.
     * @param id Recipe ID
     * @return Eagerly fetched RecipeEntity with related ingredients loaded
     */
    @Query("SELECT r FROM RecipeEntity r JOIN FETCH r.ingredients WHERE r.id = (:id)")
    Optional<RecipeEntity> findByIdAndFetchIngredientsEagerly(Long id);

    /**
     * Fetch title and ID of list of recipes identified by IDs
     * @param recipeIdSet Set of RecipeEntity IDs to be matched
     * @param pageable Pageable object to enable retrieving only a page
     * @return Titles and IDs of matching RecipeEntities
     */
    @Query(value = "SELECT r.title AS title, r.recipe_id AS id FROM Recipe_Entity r WHERE r.recipe_id in :ids",
            nativeQuery = true)
    Page<RecipeTitleDTO> findByIds(@Param("ids") Set<Long> recipeIdSet, Pageable pageable);

    @RestResource(exported = false)
    @Override
    <S extends RecipeEntity> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends RecipeEntity> Iterable<S> saveAll(Iterable<S> entities);

    @RestResource(exported = false)
    @Override
    void deleteById(Long aLong);

    @RestResource(exported = false)
    @Override
    void delete(RecipeEntity entity);

    @RestResource(exported = false)
    @Override
    void deleteAllById(Iterable<? extends Long> longs);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends RecipeEntity> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();
}
