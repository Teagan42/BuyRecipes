package rocks.teagantotally.buy_recipes.services

import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import rocks.teagantotally.buy_recipes.data.entities.Cart
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.CartRepository
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

class CartServiceTest {
  private val cartRepository = mock(CartRepository::class.java)
  private val recipeRepository = mock(RecipeRepository::class.java)
  private val productRepository = mock(ProductRepository::class.java)
  private val cartService = CartService(cartRepository, recipeRepository, productRepository)

  @BeforeEach
  fun setUp() {
    reset(cartRepository, recipeRepository, productRepository)
  }

  @Test
  fun `getCart should return existing cart`() {
    val cart = Cart(id = 1)
    `when`(cartRepository.findById(1)).thenReturn(Optional.of(cart))

    val result = cartService.getCart(1)

    assertEquals(cart, result)
  }

  @Test
  fun `getCart should return new cart if not found`() {
    `when`(cartRepository.findById(1)).thenReturn(Optional.empty())
    `when`(cartRepository.save(any(Cart::class.java))).thenReturn(Cart(id = 1))

    val result = cartService.getCart(1)

    assertEquals(1, result.id)
    assertTrue(result.items.isEmpty())
  }

  @Test
  fun `addRecipeToCart should add recipe items to cart`() {
    val cart = Cart(id = 1)
    val product = Product("Product 1", 1000, id = 1)
    val recipe = Recipe("My Family Recipe", mutableListOf(product), id = 1)
    `when`(cartRepository.findById(1)).thenReturn(Optional.of(cart))
    `when`(recipeRepository.findById(1)).thenReturn(Optional.of(recipe))
    `when`(cartRepository.save(cart)).thenReturn(cart)

    val result = cartService.addRecipeToCart(1, 1)

    assertEquals(1, result.items.size)
    assertEquals(product, result.items[0])
  }

  @Test
  fun `removeRecipeFromCart should remove recipe items from cart`() {
    val product = Product("Product 1", priceInCents = 1000, id = 1)
    val cart = Cart(mutableListOf(product), id = 1)
    val recipe = Recipe("A Saved Recipe", mutableListOf(product), id = 1)
    `when`(cartRepository.findById(1)).thenReturn(Optional.of(cart))
    `when`(recipeRepository.findById(1)).thenReturn(Optional.of(recipe))
    `when`(cartRepository.save(cart)).thenReturn(cart)

    val result = cartService.removeRecipeFromCart(1, 1)

    assertTrue(result?.items?.isEmpty() ?: false)
  }

  @Test
  fun `getCarts should return all carts`() {
    val carts = listOf(Cart(id = 1), Cart(id = 2))
    `when`(cartRepository.findAll()).thenReturn(carts)

    val result = cartService.getCarts()

    assertEquals(carts, result)
  }

  @Test
  fun `addRecipeToCart should handle non-existent recipe`() {
    val cart = Cart(id = 1)
    `when`(cartRepository.findById(1)).thenReturn(Optional.of(cart))
    `when`(recipeRepository.findById(1)).thenReturn(Optional.empty())

    val result = cartService.addRecipeToCart(1, 1)

    assertTrue(result.items.isEmpty())
  }

  @Test
  fun `removeRecipeFromCart should handle non-existent recipe`() {
    val cart = Cart(id = 1)
    `when`(cartRepository.findById(1)).thenReturn(Optional.of(cart))
    `when`(recipeRepository.findById(anyInt())).thenReturn(Optional.empty())

    val result = cartService.removeRecipeFromCart(1, 1)
    assertTrue(result?.items?.isEmpty() ?: false)
  }
}
