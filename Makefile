seed-db:
	scripts/seed_db.sh

docker-up-dev:
	docker compose -f docker-compose.dev.yml up --build

docker-down-dev:
	docker compose -f docker-compose.dev.yml down

docker-up-prod:
	docker compose up --build

docker-down-prod:
	docker compose down

docker-volume-rm-dev:
	docker compose -f docker-compose.dev.yml down -v