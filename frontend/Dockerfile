FROM node:18-alpine as builder

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY public src
COPY tsconfig.json .

RUN npm run build

RUN find . -name "*.ts" -type f -delete

FROM nginx:alpine

COPY --from=builder /app/src /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
