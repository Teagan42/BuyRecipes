package rocks.teagantotally.buy_recipes

import jakarta.annotation.PostConstruct
import java.lang.StringBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import rocks.teagantotally.buy_recipes.data.entities.Product
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.CartRepository
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

@SpringBootApplication()
class BuyRecipesApplication {
  @Autowired private lateinit var productRepository: ProductRepository

  @Autowired private lateinit var recipeRepository: RecipeRepository

  @Autowired private lateinit var cartRepository: CartRepository

  private val LOG = LoggerFactory.getLogger(this::class.java)

  @PostConstruct
  fun initializeRecords() {
    val products =
            listOf(
                    Product("Banana", 67),
                    Product("Nilla Wafers", 499),
                    Product("Vanilla Pudding", 99),
            )
    val ingredients = productRepository.saveAll(products)
    ingredients.forEach {
      LOG.info(
              StringBuilder().append(it.id).append(" ").append(it.name).toString(),
      )
    }
    val recipe = Recipe("Banana Pudding", ingredients)
    val recipes = listOf(recipeRepository.save(recipe))
    recipes.forEach {
      LOG.info(
              StringBuilder()
                      .append(it.id)
                      .append(" ")
                      .append(it.name)
                      .append(it.items.map { it.id }.joinToString())
                      .toString(),
      )
    }
    recipeRepository.findAll().forEach {
      LOG.info(
              StringBuilder()
                      .append(it.id)
                      .append(" ")
                      .append(it.name)
                      .append(it.items.map { it.id }.joinToString())
                      .toString(),
      )
    }
  }
}

fun main(args: Array<String>) {
  runApplication<BuyRecipesApplication>(*args)
}
