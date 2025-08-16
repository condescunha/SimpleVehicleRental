-- Veículos com status inicial 'AVAILABLE'
INSERT INTO vehicle (brand, fabricationYear, engine, status, model) VALUES
                                                                        ('Ford', 2024, '1.0', 'AVAILABLE', 'Ka'),
                                                                        ('Honda', 2023, '1.5', 'AVAILABLE', 'Civic'),
                                                                        ('Chevrolet', 2024, '2.0', 'AVAILABLE', 'Onix'),
                                                                        ('Fiat', 2023, '1.0', 'AVAILABLE', 'Argo'),
                                                                        ('Hyundai', 2022, '1.6', 'AVAILABLE', 'HB20'),
                                                                        ('Toyota', 2024, '1.8', 'AVAILABLE', 'Corolla');

-- Veículos com status 'UNDER_MAINTENANCE'
INSERT INTO vehicle (brand, fabricationYear, engine, status, model) VALUES
                                                                        ('Volkswagen', 2023, '1.6', 'UNDER_MAINTENANCE', 'Gol'),
                                                                        ('Renault', 2022, '1.6', 'UNDER_MAINTENANCE', 'Kwid'),
                                                                        ('Jeep', 2024, '2.0', 'UNDER_MAINTENANCE', 'Compass');