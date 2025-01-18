package rocks.teagantotally.buy_recipes.services

import org.springframework.stereotype.Service
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

/**
 * Service class for managing recipes.
 *
 * @property recipes The repository used for accessing recipe data.
 */
@Service
class RecipeService(
    private var recipes: RecipeRepository,
    private var products: ProductRepository,
) {
    /**
     * Retrieves all recipes.
     *
     * @return A list of all recipes.
     */
    fun getRecipes(): List<Recipe> = recipes.findAll()

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id The ID of the recipe to retrieve.
     * @return The recipe with the specified ID, or null if not found.
     */
    fun getRecipe(id: Int): Recipe? = recipes.findById(id).orElse(null)

    fun getProducts(): List<Product> = products.findAll()
}
