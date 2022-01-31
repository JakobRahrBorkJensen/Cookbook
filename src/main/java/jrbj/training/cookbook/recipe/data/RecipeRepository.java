package jrbj.training.cookbook.recipe.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "recipes", path = "recipes")
public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {

    @Query("SELECT r FROM RecipeEntity r JOIN FETCH r.ingredients WHERE r.id = (:id)")
    Optional<RecipeEntity> findByIdAndFetchIngredientsEagerly(Long id);

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
