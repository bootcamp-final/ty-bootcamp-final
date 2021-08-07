const calculate = require("../costCalculator")
const customRuleset = require("./stubObjects")


test('test total amount above', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": true,
    "totalAmount": 151,
    "categories": [],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(1);
});

test('test total amount below', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": true,
    "totalAmount": 49,
    "categories": [],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(2);
});

test('test elite member', () => {
  const shoppingCartDetails = {
    "isEliteMember": true,
    "totalAmount": 75,
    "categories": [],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(3);
});


test('test total shopping cart categories contains all', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": false,
    "totalAmount": 51,
    "categories": ["a", "b", "c", "d", "e"],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(4);
});

test('test categories contains at least one', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": false,
    "totalAmount": 55,
    "categories": ["a", "f", "z"],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(5);
});

test('test categories does not contains at least one', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": false,
    "totalAmount": 55,
    "categories": ["a", "b", "i", "k"],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(6);
});

test('test categories does not contains any', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": false,
    "totalAmount": 55,
    "categories": ["i", "j", "k"],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(7);
});

test('test wildcard rule', () => {
  const shoppingCartDetails = {
    "isEliteCustomer": false,
    "totalAmount": 55,
    "categories": ["i", "j", "k", "x"],
  }

  expect(calculate(shoppingCartDetails, customRuleset))
    .toBe(8);
});
