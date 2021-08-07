const request = require("supertest");
const app = require("../app");

describe("test the main entrypoint", () => {
  test("It should response with 200", async () => {
    const payload = {
      "isEliteMember": false,
      "totalAmount": 0,
      "categories": []
    }

    const response = await request(app)
      .post("/")
      .send(payload)
      .timeout(10000);

    expect(response.statusCode).toBe(200);
  });
});
