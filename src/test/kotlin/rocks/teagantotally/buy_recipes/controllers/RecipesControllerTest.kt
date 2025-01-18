package rocks.teagantotally.buy_recipes.controllers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.services.RecipeService

class RecipesControllerTest {
  private val recipeService = mock(RecipeService::class.java)
  private val recipesController = RecipesController(recipeService)
  private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(recipesController).build()

  @Test
  fun `getRecipes should return list of recipes`() {
    val recipes = listOf(Recipe(id = 1, name = "Recipe 1"), Recipe(id = 2, name = "Recipe 2"))
    `when`(recipeService.getRecipes()).thenReturn(recipes)

    mockMvc.perform(get("/recipes").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[0].id").value(1))
      .andExpect(jsonPath("$[0].name").value("Recipe 1"))
      .andExpect(jsonPath("$[1].id").value(2))
      .andExpect(jsonPath("$[1].name").value("Recipe 2"))
  }

  @Test
  fun `getRecipe should return recipe by id`() {
    val recipe = Recipe(id = 1, name = "Recipe 1")
    `when`(recipeService.getRecipe(1)).thenReturn(recipe)

    mockMvc.perform(get("/recipes/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.name").value("Recipe 1"))
  }

  @Test
  fun `getRecipe should return 404 if recipe not found`() {
    `when`(recipeService.getRecipe(1)).thenReturn(null)

    mockMvc.perform(get("/recipes/1").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound)
  }
}