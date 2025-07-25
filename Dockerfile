FROM node:20-alpine as BUILDSTAGE

WORKDIR /app

COPY ./ /app/

RUN npm i && npm run build

FROM node:20-alpine 

WORKDIR /app

COPY --from=BUILDSTAGE /app/.dist ./
COPY --from=BUILDSTAGE /app/node_modules ./

CMD ["node", ".dist/index.js"]