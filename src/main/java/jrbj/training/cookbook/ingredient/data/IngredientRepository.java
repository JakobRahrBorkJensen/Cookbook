package jrbj.training.cookbook.ingredient.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "ingredients", path = "ingredients")
public interface IngredientRepository extends PagingAndSortingRepository<IngredientEntity, Long> {

    @RestResource(exported = false)
    @Override
    <S extends IngredientEntity> S save(S entity);

    @RestResource(exported = false)
    @Override
    <S extends IngredientEntity> Iterable<S> saveAll(Iterable<S> entities);

    @RestResource(exported = false)
    @Override
    void deleteById(Long aLong);

    @RestResource(exported = false)
    @Override
    void delete(IngredientEntity entity);

    @RestResource(exported = false)
    @Override
    void deleteAllById(Iterable<? extends Long> longs);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends IngredientEntity> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();
}
