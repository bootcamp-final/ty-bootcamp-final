const customRuleset = [
  {
    "rule": ["toplam tutar", "fazla", 150.0],
    "cost": 1
  },
  {
    "rule": ["toplam tutar", "az", 50.0],
    "cost": 2
  },
  {
    "rule": ["elit üyelik", "var"],
    "cost": 3
  },
  {
    "rule": ["sepet ürünlerinden", "hepsini içeren", ["a", "b", "c"]],
    "cost": 4
  },
  {
    "rule": ["sepet ürünlerinden", "herhangi birini içeren", ["e", "f", "g"]],
    "cost": 5
  },
  {
    "rule": ["sepet ürünlerinden", "herhangi birini içermeyen", ["i", "j", "k"]],
    "cost": 6
  },
  {
    "rule": ["sepet ürünlerinden", "hiçbirini içermeyen", ["x", "y", "z"]],
    "cost": 7
  },
  {
    "rule": ["*"],
    "cost": 8
  }
]

module.exports = customRuleset
