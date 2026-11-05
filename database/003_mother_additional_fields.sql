ALTER TABLE public.mothers
  ADD COLUMN IF NOT EXISTS expected_delivery_date date,
  ADD COLUMN IF NOT EXISTS last_menstrual_period date,
  ADD COLUMN IF NOT EXISTS sub_county varchar(100);
