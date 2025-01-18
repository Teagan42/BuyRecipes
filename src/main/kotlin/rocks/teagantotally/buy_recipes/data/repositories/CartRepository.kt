package rocks.teagantotally.buy_recipes.data.repositories

import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository
import rocks.teagantotally.buy_recipes.data.entities.Cart

@Repository interface CartRepository : ListCrudRepository<Cart, Int> {}
