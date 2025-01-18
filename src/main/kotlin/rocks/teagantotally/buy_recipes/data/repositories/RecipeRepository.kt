package rocks.teagantotally.buy_recipes.data.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import rocks.teagantotally.buy_recipes.data.entities.Recipe

@Repository interface RecipeRepository : JpaRepository<Recipe, Int> {}
