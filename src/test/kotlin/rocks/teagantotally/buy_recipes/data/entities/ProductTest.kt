package rocks.teagantotally.buy_recipes.data.entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductTest {
  @Test
  fun `test product creation with all properties`() {
    val product = Product(id = 1, name = "Test Product", priceInCents = 1000)
    assertNotNull(product)
    assertEquals(1, product.id)
    assertEquals("Test Product", product.name)
    assertEquals(1000, product.priceInCents)
  }

  @Test
  fun `test product creation without id`() {
    val product = Product(name = "Test Product", priceInCents = 1000)
    assertNotNull(product)
    assertNull(product.id)
    assertEquals("Test Product", product.name)
    assertEquals(1000, product.priceInCents)
  }

  @Test
  fun `test product creation with items`() {
    val recipe1 = Recipe(id = 1, name = "Recipe 1")
    val recipe2 = Recipe(id = 2, name = "Recipe 2")
    val product =
            Product(
                    id = 1,
                    name = "Test Product",
                    priceInCents = 1000,
                    recipes = mutableListOf(recipe1, recipe2)
            )
    assertNotNull(product)
    assertEquals(1, product.id)
    assertEquals("Test Product", product.name)
    assertEquals(1000, product.priceInCents)
    assertEquals(2, product.recipes.size)
    assertTrue(product.recipes.contains(recipe1))
    assertTrue(product.recipes.contains(recipe2))
  }

  @Test
  fun `test product creation without items`() {
    val product = Product(id = 1, name = "Test Product", priceInCents = 1000)
    assertNotNull(product)
    assertEquals(1, product.id)
    assertEquals("Test Product", product.name)
    assertEquals(1000, product.priceInCents)
    assertTrue(product.recipes.isEmpty())
  }
}
