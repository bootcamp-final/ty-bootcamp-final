const express = require("express");

const PORT = process.env.PORT || 7878

const app = express();
app.use(express.json())

const server = app.listen(PORT, () => console.info(`Server listening on port ${PORT}`))





process.on('uncaughtException', () => {
  if (server) server.close()
})

process.on('SIGTERM', () => {
  if (server) server.close()
})

