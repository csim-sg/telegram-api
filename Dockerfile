FROM node:20-alpine AS buildStage

WORKDIR /app

COPY ./ /app/

RUN apk add --no-cache \
    python3 \
    make \
    g++ \
    bash

RUN npm i
RUN npm run build

FROM node:20-alpine

WORKDIR /app

COPY --from=buildStage /app/.dist ./.dist
COPY --from=buildStage /app/node_modules ./node_modules

CMD ["node", ".dist/index.js"]