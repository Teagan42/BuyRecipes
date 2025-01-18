package rocks.teagantotally.buy_recipes.controllers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import rocks.teagantotally.buy_recipes.data.entities.Cart
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.services.CartService

class CartsControllerTest {
  private val cartService = mock(CartService::class.java)
  private val cartsController = CartsController(cartService)
  private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(cartsController).build()

  @BeforeEach
  fun setUp() {
    reset(cartService)
  }

  @Test
  fun `getCarts should return list of carts`() {
    val carts = listOf(Cart(id = 1), Cart(id = 2))
    `when`(cartService.getCarts()).thenReturn(carts)

    mockMvc.perform(get("/carts"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
  }

  @Test
  fun `getCart should return cart by id`() {
    val cart = Cart(id = 1)
    `when`(cartService.getCart(1)).thenReturn(cart)

    mockMvc.perform(get("/carts/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
  }

  @Test
  fun `getCart should return null if cart not found`() {
    `when`(cartService.getCart(1)).thenReturn(null)

    mockMvc.perform(get("/carts/1")).andExpect(status().isOk).andExpect(content().string(""))
  }

  @Test
  fun `addRecipeToCart should add recipe to cart by recipe object`() {
    val cart = Cart(id = 1, items = mutableListOf(Product("Product 1", 1000)))
    val recipe = Recipe("My Family Recipe", mutableListOf(Product("Product 1", 1000)), 1)
    `when`(cartService.addRecipeToCart(1, recipe)).thenReturn(cart)

    mockMvc.perform(
                    post("/carts/1/add_recipe")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"My Family Recipe\",\"id\": 1, \"items\":[{\"name\":\"Product 1\",\"priceInCents\":1000, \"id\": 3}]}"
                            ),
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("Product 1"))
  }

  @Test
  fun `addRecipeToCart should add recipe to cart by recipe id`() {
    val cart = Cart(id = 1, items = mutableListOf(Product("Product 1", 1000)))
    `when`(cartService.addRecipeToCart(1, 1)).thenReturn(cart)

    mockMvc.perform(post("/carts/1/add_recipe/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.items[0].name").value("Product 1"))
  }

  @Test
  fun `removeRecipeFromCart should remove recipe from cart`() {
    val cart = Cart(id = 1, items = mutableListOf())
    `when`(cartService.removeRecipeFromCart(1, 1)).thenReturn(cart)

    mockMvc.perform(delete("/carts/1/recipes/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.items").isEmpty)
  }
}
