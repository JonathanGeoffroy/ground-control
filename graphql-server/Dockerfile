FROM node:alpine AS graphql-builder
COPY . /app
COPY ./.env.production /app/.env
COPY --from=ground-control_planet-builder /app/target/docs/schema.json /app/schema.json
WORKDIR /app
RUN npm run build:all

FROM node:alpine
WORKDIR /app
COPY --from=graphql-builder /app/dist .
COPY --from=graphql-builder /app/.env ./.env

EXPOSE 4000
CMD ["node", "./index.js"]
