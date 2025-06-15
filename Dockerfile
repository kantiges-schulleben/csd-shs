FROM node:20-alpine

RUN apk add --no-cache python3 py3-pip

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npx tsc

CMD ["node", "src/server.js"]
