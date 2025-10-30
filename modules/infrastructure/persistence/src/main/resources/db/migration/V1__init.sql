CREATE TABLE IF NOT EXISTS area (
    id BIGINT PRIMARY KEY,
    parent_sigungu_id BIGINT DEFAULT NULL,
    sigungu_name VARCHAR(255) DEFAULT NULL,
    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL
);

CREATE TABLE IF NOT EXISTS address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL,
    lat DOUBLE NOT NULL,
    lon DOUBLE NOT NULL,
    name VARCHAR(255) NULL,
    post_code VARCHAR(255) NULL,
    area_id BIGINT NULL,
    INDEX idx_address_area_id (area_id),
    CONSTRAINT fk_address_area
        FOREIGN KEY (area_id)
        REFERENCES area (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table if not exists passenger(
    id bigint auto_increment primary key,
    user_type enum('GOOGLE', 'GUEST', 'KAKAO') default null,
    is_login bit(1) default null
);

create table if not exists transport_operator(
    id bigint auto_increment primary key,
    login_id VARCHAR(255) default null,
    password VARCHAR(255) default null,
    operator_name VARCHAR(255) default null
);

create table if not exists local_government(
    id bigint auto_increment primary key,
    login_id VARCHAR(255) default null,
    password VARCHAR(255) default null,
    operator_name VARCHAR(255) default null
);

create table if not exists route(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL,
    local_gov_id BIGINT NOT NULL,
    operator_id BIGINT NOT NULL
);


create table if not exists route_component(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL,
    assigned_time DATETIME NOT NULL,
    route_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS route_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL,
    departure_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    end_date_time DATETIME(6) NOT NULL,
    passenger_id BIGINT NOT NULL,
    start_date_time DATETIME(6) NOT NULL,
    status VARCHAR(255) NULL,
    CONSTRAINT fk_route_request_departure
        FOREIGN KEY (departure_id)
        REFERENCES address(id),
    CONSTRAINT fk_route_request_destination
        FOREIGN KEY (destination_id)
        REFERENCES address(id),
    CONSTRAINT fk_route_request_passenger
        FOREIGN KEY (passenger_id)
        REFERENCES passenger(id)
);

CREATE TABLE IF NOT EXISTS bus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator_id BIGINT NOT NULL,
    busNumber INT NOT NULL,
    carNumber VARCHAR(20) NOT NULL,
    status enum('IDLE', 'OPERATING') default 'IDLE',
);
