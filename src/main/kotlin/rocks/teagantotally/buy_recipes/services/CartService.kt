package rocks.teagantotally.buy_recipes.services

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import rocks.teagantotally.buy_recipes.data.entities.Cart
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.CartRepository
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

/**
 * Service class for managing shopping carts.
 *
 * @property carts The repository for accessing and managing carts.
 * @property recipes The repository for accessing and managing recipes.
 * @property products The repository for accessing and managing products.
 */
@Service
class CartService(
        private var carts: CartRepository,
        private var recipes: RecipeRepository,
        private var products: ProductRepository,
        @PersistenceContext private var entityManager: EntityManager,
) {
  fun getCarts(): List<Cart> = carts.findAll()

  /**
   * Retrieves a cart by its ID.
   *
   * @param id The ID of the cart to retrieve.
   * @return The cart with the specified ID, or a new cart if not found.
   */
  fun getCart(id: Int): Cart =
          carts.findById(id).orElse(Cart(id = id)).apply {
            items = products.findAllByCartsId(id).toMutableList()
          }

  /**
   * Adds a recipe to a cart by their IDs.
   *
   * @param id The ID of the cart.
   * @param recipeId The ID of the recipe to add.
   * @return The updated cart.
   */
  @Transactional
  fun addRecipeToCart(
          id: Int,
          recipeId: Int,
  ): Cart = addRecipeToCart(id, recipes.findById(recipeId).orElse(null))

  /**
   * Adds a recipe to a cart.
   *
   * @param id The ID of the cart.
   * @param recipe The recipe to add.
   * @return The updated cart.
   */
  @Transactional
  fun addRecipeToCart(
          id: Int,
          recipe: Recipe?,
  ): Cart {
    val productIds = recipe?.items?.map { it.id } ?: emptyList()
    if (productIds.isEmpty()) {
      return getCart(id)
    }
    val products = products.findAllById(productIds.asIterable())
    return carts.findById(id)
            .map { it.apply { items.addAll(products) } }
            .orElse(Cart(items = products, id = id))
            .let { carts.save(it) }
  }

  /**
   * Removes a recipe from a cart by their IDs.
   *
   * @param id The ID of the cart.
   * @param recipeId The ID of the recipe to remove.
   * @return The updated cart, or null if the cart was not found.
   */
  @Transactional
  fun removeRecipeFromCart(
          id: Int,
          recipeId: Int,
  ): Cart =
          getCart(id)
                  .apply {
                    recipes.findById(recipeId).orElse(null)?.let { items.removeAll(it.items) }
                  }
                  .let { carts.save(it) }

  /**
   * Retrieves the items of a recipe by its ID.
   *
   * @param id The ID of the recipe to retrieve.
   * @return A list of items in the recipe if found, otherwise an empty list.
   */
  private fun getRecipeProducts(recipeId: Int): List<Product> =
          recipes.findById(recipeId).orElse(null)?.items ?: emptyList()
}
