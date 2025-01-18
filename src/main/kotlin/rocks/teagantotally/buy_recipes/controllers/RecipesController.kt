package rocks.teagantotally.buy_recipes.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.services.RecipeService

/**
 * REST controller for managing recipes.
 *
 * This controller provides endpoints to retrieve recipes.
 *
 * @param recipeService The service used to manage recipes.
 */
@RestController
@RequestMapping("/recipes", produces = ["application/json"])
class RecipesController(
        private val recipeService: RecipeService,
) {
  /**
   * Retrieves a list of all recipes.
   *
   * @return A list of recipes.
   */
  @GetMapping() @ResponseBody fun getRecipes(): List<Recipe> = recipeService.getRecipes()

  /**
   * Retrieves a specific recipe by its ID.
   *
   * @param id The ID of the recipe to retrieve.
   * @return The recipe with the specified ID, or null if not found.
   */
  @GetMapping("/{id}")
  @ResponseBody
  fun getRecipe(
          @PathVariable id: Int,
  ): Recipe? = recipeService.getRecipe(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}
