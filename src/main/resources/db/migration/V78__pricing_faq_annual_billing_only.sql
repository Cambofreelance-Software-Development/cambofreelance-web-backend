-- Subscriptions are annual billing only; monthly is not a selectable option at checkout.
-- Correct the seeded FAQ copy from V59 which advertised a monthly-or-yearly choice.
UPDATE public.pricing_faq
SET answer = 'Open the Subscription page from your dashboard, pick the plan that fits your business, and complete payment. Billing is annual, and your new plan activates immediately after payment is confirmed.',
    answer_kh = 'បើកទំព័រ Subscription ពីផ្ទាំងគ្រប់គ្រងរបស់អ្នក ជ្រើសរើសគម្រោងដែលសាកសមនឹងអាជីវកម្មរបស់អ្នក រួចបញ្ចប់ការទូទាត់។ ការទូទាត់ជាប្រចាំឆ្នាំ ហើយគម្រោងថ្មីរបស់អ្នកនឹងចាប់ផ្តើមដំណើរការភ្លាមៗបន្ទាប់ពីការទូទាត់ត្រូវបានបញ្ជាក់។'
WHERE answer LIKE '%choose monthly or yearly billing%';
