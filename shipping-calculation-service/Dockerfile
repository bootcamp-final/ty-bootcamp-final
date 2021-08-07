FROM node:12-alpine

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install

COPY . .
EXPOSE $PORT
RUN npm test

CMD ["npm", "start"]
