package rocks.teagantotally.buy_recipes.data.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecipeTest {
    @Test
    fun `recipe should have correct name`() {
        val recipe = Recipe("Test Recipe", mutableListOf())

        assertEquals("Test Recipe", recipe.name)
    }

    @Test
    fun `recipe should have correct items`() {
        val product1 = Product(id = 1, name = "Product 1", priceInCents = 1000)
        val product2 = Product(id = 2, name = "Product 2", priceInCents = 2000)
        val recipe = Recipe(name = "Test Recipe", items = mutableListOf(product1, product2))

        assertEquals(2, recipe.items.size)
        assertEquals(product1, recipe.items[0])
        assertEquals(product2, recipe.items[1])
    }

    @Test
    fun `recipe should handle empty items list`() {
        val recipe = Recipe(name = "Test Recipe", items = mutableListOf())

        assertEquals(0, recipe.items.size)
    }
}
