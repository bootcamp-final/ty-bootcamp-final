const fs = require('fs')

const ruleFilePath = process.env.RULE_FILE || "offering-rules.json";

function calculate(shoppingCartDetails, rules = undefined) {
  let offeringRules
  if (rules === undefined) {
    offeringRules = loadRulesFromJsonFile()
  } else {
    offeringRules = rules
  }

  for (const offeringRule of offeringRules) {
    if (doesMatch(offeringRule.rule, shoppingCartDetails))
      return offeringRule.cost
  }
  return -1
}

function loadRulesFromJsonFile() {
  const jsonString = fs.readFileSync(ruleFilePath, {
    encoding: "utf8", flag: "r"
  })
  const rules = JSON.parse(jsonString)
  rules.sort((a, b) => a.cost - b.cost)
  return rules
}

function doesMatch(rule, shoppingCartDetails) {
  const [second, third, fourth] = rule;
  switch (second) {
    case "toplam tutar":
      const totalAmountConditionFunc = totalAmountConditionFunctions[third];
      return totalAmountConditionFunc(fourth, shoppingCartDetails.totalAmount)
    case "sepet ürünlerinden":
      const conditionFunc = categoryConditionFunctions[third]
      return conditionFunc(fourth, shoppingCartDetails.categories)
    case "elit üyelik":
      return eliteMembershipFunc(third, shoppingCartDetails.isEliteMember)
    case "*":
      return true
    default:
      return false
  }
}

const eliteMembershipFunc = function (exists, isEliteMember) {
  if (exists === "var")
    return isEliteMember
  else
    return !isEliteMember
}

const totalAmountConditionFunctions = {
  "büyüktür": (requiredAmount, shoppingCartAmount) => shoppingCartAmount > requiredAmount,
  "küçüktür": (requiredAmount, shoppingCartAmount) => shoppingCartAmount < requiredAmount,
}

const categoryConditionFunctions = {
  "en az birini içeren": containsAtLeastOne,
  "en az birini içermeyen": doesNotContainsAtLeastOne,
  "hepsini içeren": containsAll,
  "hiçbirini içermeyen": doesNotContainsAny
}

function containsAtLeastOne(listA, listB) {
  for (const elem of listA) {
    if (listB.includes(elem))
      return true
  }
  return false
}

function doesNotContainsAny(listA, listB) {
  return !containsAtLeastOne(listA, listB)
}

function containsAll(listA, listB) {
  for (const elem of listA) {
    if (!listB.includes(elem))
      return false
  }
  return true
}

function doesNotContainsAtLeastOne(listA, listB) {
  return !containsAll(listA, listB)
}

module.exports = calculate
