package rocks.teagantotally.buy_recipes.services

import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

@ExtendWith(MockitoExtension::class)
class RecipeServiceTest {
  @Mock private lateinit var recipeRepository: RecipeRepository

  @Mock private lateinit var productRepository: ProductRepository

  @InjectMocks private lateinit var recipeService: RecipeService

  @BeforeEach
  fun setUp() {
    reset(recipeRepository, productRepository)
  }

  @Test
  fun `getRecipes should return all recipes`() {
    val recipes =
            listOf(
                    Recipe("Recipe 1", mutableListOf(), 1),
                    Recipe("Recipe 2", mutableListOf(), 2),
            )
    whenever(recipeRepository.findAll()).thenReturn(recipes)

    val result = recipeService.getRecipes()

    assertEquals(recipes, result)
  }

  @Test
  fun `getRecipe should return recipe by ID`() {
    val recipe = Recipe(id = 1, name = "Recipe 1")
    whenever(recipeRepository.findById(1)).thenReturn(Optional.of(recipe))

    val result = recipeService.getRecipe(1)

    assertEquals(recipe, result)
  }

  @Test
  fun `getRecipe should return null if recipe not found`() {
    whenever(recipeRepository.findById(1)).thenReturn(Optional.empty())

    val result = recipeService.getRecipe(1)

    assertNull(result)
  }

  @Test
  fun `getProducts should return all products`() {
    val products =
            listOf(
                    Product(id = 1, name = "Product 1", priceInCents = 1000),
                    Product(id = 2, name = "Product 2", priceInCents = 2000),
            )
    whenever(productRepository.findAll()).thenReturn(products)

    val result = recipeService.getProducts()

    assertEquals(products, result)
  }
}
