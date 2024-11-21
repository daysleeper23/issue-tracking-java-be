INSERT INTO roles_permissions (id, permission_id, role_id)
VALUES
    (gen_random_uuid(), '00f74750-4f78-460e-adc2-70f007fdfd1e', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '281da56c-d00c-460f-be6a-990db44ddc34', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '84c0d22c-81fe-43ec-9978-5e3638b49ab7', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '83965484-5006-4d70-b963-a20bebf2dc3e', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '2b86782c-90ec-4f20-a4d0-fd20972b59ef', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'e6894dd8-1aa2-4755-a179-8bde5f087f15', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'de27469d-69bd-46d9-bf10-03290221d8ff', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '7508cdf3-b617-454a-a106-047438959599', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '6c7b8378-a952-47a4-b9c5-fb7c2a090541', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'f255dd47-f282-4aec-ae22-6c44fbf1d933', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '4272b481-c039-4712-950e-e278aebe62b0', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '7173cd35-e3eb-4019-956c-f9e1d07ed628', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '05a1d5ab-06ae-4301-8885-83ef33cf0a9d', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'a3c4ef1f-0118-439f-9161-4378c43b11f9', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'b5994a72-4ab7-44e4-bb42-8667c1e2e9d2', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'd98f4f30-8cfa-4569-a864-49e1916ee715', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '8946ddd3-bc4b-4951-8df9-8227e28b9a4e', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '1e8cdfaa-4bd4-4111-866f-6292f26d97f1', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), '95e4b214-503d-4c80-bd3d-6f2fad77098f', '745b3885-cb08-4076-b97e-626c56e10e0f'),
    (gen_random_uuid(), 'd490bcf9-faef-4230-b575-ba72f3a90f61', '745b3885-cb08-4076-b97e-626c56e10e0f');

INSERT INTO company_managers (id, company_id, role_id, user_id)
VALUES
    (gen_random_uuid(), '3bbb31a7-0915-4d77-b185-e939a5b9cd38', '745b3885-cb08-4076-b97e-626c56e10e0f', 'acd9c8ef-87f3-4eae-bd92-238b01b746b6');