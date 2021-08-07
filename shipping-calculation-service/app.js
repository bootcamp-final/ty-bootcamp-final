const express = require("express");
const calculator = require("./costCalculator")

const app = express();
app.use(express.json())


app.post("/", async (req, res) => {
  const shoppingCartDetails = req.body;
  const shippingCost = calculator(shoppingCartDetails)

  if (shippingCost < 0)
    return res.send().status(400)

  return res.send({shippingCost})
})

module.exports = app
