-- Static seed articles for CamboFreelance platform.
-- All articles are published and active. Khmer titles/excerpts are provided where applicable.

INSERT INTO public.articles (
    id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
    type, author_name, tags, published_at, sort_order,
    workflow_status, status, created_by
) VALUES

-- ── NEWS ──────────────────────────────────────────────────────────────────────
(
    gen_random_uuid(),
    'CamboFreelance Platform Officially Launches',
    'វេទិកា CamboFreelance បើកដំណើរការជាផ្លូវការ',
    'cambofreelance-platform-officially-launches',
    'We are thrilled to announce the official launch of CamboFreelance, Cambodia''s first dedicated freelance marketplace connecting talented professionals with businesses.',
    'យើងមានការរំភើបក្នុងការប្រកាសការបើកដំណើរការជាផ្លូវការរបស់ CamboFreelance ទីផ្សារសេរីភាពដំបូងគេរបស់កម្ពុជា។',
    '<p>We are thrilled to announce the official launch of <strong>CamboFreelance</strong>, Cambodia''s first dedicated freelance marketplace. Our platform connects talented Cambodian professionals with businesses seeking quality services.</p><p>From web development to graphic design, content writing to digital marketing — CamboFreelance is your one-stop destination for skilled freelancers across every industry.</p><h2>What We Offer</h2><ul><li>Verified freelancer profiles with skills assessments</li><li>Secure payment escrow system</li><li>Real-time project collaboration tools</li><li>24/7 customer support in English and Khmer</li></ul><p>Join thousands of freelancers and businesses already transforming the way Cambodia works.</p>',
    '<p>យើងមានការរំភើបក្នុងការប្រកាសការបើកដំណើរការជាផ្លូវការរបស់ <strong>CamboFreelance</strong> ទីផ្សារសេរីភាពដំបូងគេរបស់កម្ពុជា។ វេទិការបស់យើងភ្ជាប់អ្នកជំនាញខ្មែរដែលមានទេពកោសល្យជាមួយអាជីវកម្មដែលស្វែងរកសេវាកម្មមានគុណភាព។</p>',
    'NEWS',
    'CamboFreelance Team',
    'launch,platform,news,cambodia',
    NOW(),
    1,
    'PUBLISHED', 'ACT', 'SYS'
),
(
    gen_random_uuid(),
    'New Payment Gateway Integration for Faster Payouts',
    'ការភ្ជាប់ច្រកទូទាត់ប្រាក់ថ្មីសម្រាប់ការបង់ប្រាក់លឿនជាងមុន',
    'new-payment-gateway-integration-faster-payouts',
    'CamboFreelance now supports ABA Pay, KHQR, and Wing transfers for seamless freelancer payouts across Cambodia.',
    'CamboFreelance ឥឡូវនេះគាំទ្រការទូទាត់ ABA Pay, KHQR និង Wing សម្រាប់ការបង់ប្រាក់ដោយរលូនដល់អ្នកធ្វើការសេរីពាសពេញប្រទេសកម្ពុជា។',
    '<p>We''re excited to announce expanded payment options for all CamboFreelance users. Starting today, freelancers can receive payouts via:</p><ul><li><strong>ABA Pay</strong> — instant transfers to any ABA account</li><li><strong>KHQR</strong> — scan-to-receive from any participating bank</li><li><strong>Wing</strong> — mobile money transfers across Cambodia</li><li><strong>Bank Transfer</strong> — all major Cambodian banks supported</li></ul><p>Minimum payout threshold has been reduced to $5, making it easier for new freelancers to access their earnings quickly.</p>',
    '<p>យើងរំភើបដែលប្រកាសពីជម្រើសទូទាត់ប្រាក់ដែលបានពង្រីកសម្រាប់អ្នកប្រើប្រាស់ CamboFreelance ទាំងអស់។</p>',
    'NEWS',
    'CamboFreelance Team',
    'payment,aba,khqr,wing,payout',
    NOW() - INTERVAL '7 days',
    2,
    'PUBLISHED', 'ACT', 'SYS'
),

-- ── ANNOUNCEMENTS ─────────────────────────────────────────────────────────────
(
    gen_random_uuid(),
    'Platform Maintenance Scheduled – June 30, 2026',
    'ការថែទាំប្រព័ន្ធត្រូវបានកំណត់ – ថ្ងៃទី ៣០ មិថុនា ២០២៦',
    'platform-maintenance-scheduled-june-30-2026',
    'CamboFreelance will undergo scheduled maintenance on June 30, 2026 from 2:00 AM to 4:00 AM ICT. Services will be temporarily unavailable during this window.',
    'CamboFreelance នឹងធ្វើការថែទាំតាមកាលវិភាគនៅថ្ងៃទី ៣០ មិថុនា ២០២៦ ចាប់ពីម៉ោង ២:00 ព្រឹក ដល់ ៤:00 ព្រឹក ICT។',
    '<p>Dear CamboFreelance community,</p><p>We will be performing scheduled system maintenance to improve platform performance and security.</p><p><strong>Maintenance Window:</strong><br>Date: June 30, 2026<br>Time: 2:00 AM – 4:00 AM (Indochina Time, GMT+7)</p><p><strong>What to expect:</strong></p><ul><li>The platform will be inaccessible during maintenance</li><li>All active projects and messages are safe — no data will be lost</li><li>Payment processing will resume immediately after maintenance</li></ul><p>We recommend saving any ongoing work before the maintenance window. Thank you for your patience.</p>',
    '<p>សហគមន៍ CamboFreelance ជាទីមោទនៈ យើងនឹងធ្វើការថែទាំប្រព័ន្ធតាមកាលវិភាគ។</p>',
    'ANNOUNCEMENTS',
    'CamboFreelance Team',
    'maintenance,downtime,announcement',
    NOW() - INTERVAL '3 days',
    1,
    'PUBLISHED', 'ACT', 'SYS'
),
(
    gen_random_uuid(),
    'Updated Terms of Service – Effective July 1, 2026',
    'លក្ខខណ្ឌសេវាកម្មដែលបានធ្វើបច្ចុប្បន្នភាព – មានប្រសិទ្ធភាពពីថ្ងៃទី ១ កក្កដា ២០២៦',
    'updated-terms-of-service-effective-july-2026',
    'We have updated our Terms of Service to reflect new features, improved dispute resolution, and compliance with Cambodian e-commerce regulations.',
    'យើងបានធ្វើបច្ចុប្បន្នភាពលក្ខខណ្ឌសេវាកម្មរបស់យើងដើម្បីឆ្លុះបញ្ចាំងពីមុខងារថ្មី ការដោះស្រាយវិវាទប្រសើរ និងការអនុលោមតាមបទប្បញ្ញត្តិពាណិជ្ជកម្មអេឡិចត្រូនិករបស់កម្ពុជា។',
    '<p>CamboFreelance is updating its Terms of Service effective <strong>July 1, 2026</strong>. Key changes include:</p><ul><li><strong>Dispute Resolution</strong> — A new 3-step mediation process replacing the existing 2-step process</li><li><strong>Service Fees</strong> — Reduced platform fee from 15% to 10% for freelancers with 5+ completed projects</li><li><strong>Intellectual Property</strong> — Clearer ownership transfer clauses upon full payment</li><li><strong>Data Privacy</strong> — Enhanced alignment with Cambodia''s Data Privacy Law</li></ul><p>Please review the full updated Terms of Service in your account settings. Continued use of the platform after July 1 constitutes acceptance of the new terms.</p>',
    NULL,
    'ANNOUNCEMENTS',
    'CamboFreelance Legal Team',
    'legal,terms,policy,compliance',
    NOW() - INTERVAL '5 days',
    2,
    'PUBLISHED', 'ACT', 'SYS'
),

-- ── PROMOTIONS ────────────────────────────────────────────────────────────────
(
    gen_random_uuid(),
    'Zero Platform Fee for First 3 Projects – New Freelancer Offer',
    'គ្មានថ្លៃវេទិកាសម្រាប់គម្រោង ៣ ដំបូង – ការផ្តល់ជូនដល់អ្នកធ្វើការសេរីថ្មី',
    'zero-platform-fee-first-3-projects-new-freelancer',
    'New freelancers joining CamboFreelance before July 31, 2026 enjoy zero platform fees on their first three completed projects.',
    'អ្នកធ្វើការសេរីថ្មីដែលចូលរួម CamboFreelance មុនថ្ងៃទី ៣១ កក្កដា ២០២៦ ទទួលបានការលើកលែងថ្លៃវេទិកាសម្រាប់គម្រោងដំបូងបីរបស់ពួកគេ។',
    '<p>Starting your freelance journey? CamboFreelance is making it easier than ever to get started with our <strong>New Freelancer Zero Fee</strong> promotion.</p><h2>How It Works</h2><ol><li>Create your verified freelancer profile before <strong>July 31, 2026</strong></li><li>Complete your profile with a portfolio and skills assessment</li><li>Your first 3 completed projects earn 100% — no platform fee deducted</li></ol><h2>Eligibility</h2><ul><li>New accounts registered after June 1, 2026</li><li>Projects must be completed and payment released within 90 days</li><li>One promotion per freelancer account</li></ul><p>Don''t miss this opportunity to build your reputation and keep every dollar you earn on your first three projects.</p>',
    '<p>ចាប់ផ្តើមការធ្វើការសេរីរបស់អ្នក? CamboFreelance កំពុងធ្វើឱ្យវាកាន់តែងាយស្រួលជាមួយការផ្សព្វផ្សាយ <strong>Zero Fee</strong> របស់យើង។</p>',
    'PROMOTIONS',
    'CamboFreelance Marketing',
    'promotion,new-freelancer,fee,offer',
    NOW() - INTERVAL '2 days',
    1,
    'PUBLISHED', 'ACT', 'SYS'
),
(
    gen_random_uuid(),
    'Summer Business Package – 20% Off Premium Subscriptions',
    'កញ្ចប់អាជីវកម្មរដូវក្តៅ – បញ្ចុះតម្លៃ ២០% លើការជាវពិសេស',
    'summer-business-package-20-off-premium-subscriptions',
    'Businesses upgrading to Premium or Enterprise plans in June and July 2026 receive a 20% discount for the first 3 months.',
    'អាជីវកម្មដែលដំឡើងទៅផែនការ Premium ឬ Enterprise ក្នុងខែមិថុនា និងកក្កដា ២០២៦ ទទួលបានការបញ្ចុះតម្លៃ ២០% សម្រាប់ ៣ ខែដំបូង។',
    '<p>Grow your team with top Cambodian talent this summer. Upgrade your CamboFreelance business subscription and save.</p><h2>Summer Promotion Details</h2><table><tr><th>Plan</th><th>Regular Price</th><th>Summer Price</th></tr><tr><td>Premium</td><td>$49/mo</td><td>$39/mo</td></tr><tr><td>Enterprise</td><td>$149/mo</td><td>$119/mo</td></tr></table><p><strong>Offer valid:</strong> June 1 – July 31, 2026<br><strong>Discount applies:</strong> First 3 months of new subscription<br><strong>No coupon code needed</strong> — discount applied automatically at checkout.</p><p>Premium and Enterprise plans include unlimited job postings, priority freelancer matching, dedicated account manager, and advanced analytics.</p>',
    NULL,
    'PROMOTIONS',
    'CamboFreelance Marketing',
    'promotion,subscription,discount,business',
    NOW() - INTERVAL '4 days',
    2,
    'PUBLISHED', 'ACT', 'SYS'
),

-- ── BLOGS ─────────────────────────────────────────────────────────────────────
(
    gen_random_uuid(),
    '10 Tips for Writing a Winning Freelancer Profile',
    'គន្លឹះ ១០ ក្នុងការសរសេរប្រវត្តិអ្នកធ្វើការសេរីឱ្យទទួលបានជោគជ័យ',
    '10-tips-writing-winning-freelancer-profile',
    'Your profile is your storefront. Learn how to craft a compelling CamboFreelance profile that attracts high-quality clients and commands premium rates.',
    'ប្រវត្តិរបស់អ្នកគឺជាហាងរបស់អ្នក។ រៀនពីរបៀបបង្កើតប្រវត្តិ CamboFreelance ដ៏គួរឱ្យទាក់ទាញ។',
    '<p>First impressions count. In the competitive freelance marketplace, your profile is often the deciding factor between landing a project or being passed over. Here are 10 proven tips from top-earning CamboFreelance professionals:</p><h2>1. Use a Professional Photo</h2><p>A clear, friendly headshot increases profile views by up to 40%. Avoid group photos, filters, or low-quality images.</p><h2>2. Write a Compelling Headline</h2><p>Your headline should immediately communicate your specialty. Instead of "Web Developer," try "React & Node.js Developer | 5+ Years Building Scalable Web Apps."</p><h2>3. Quantify Your Experience</h2><p>Numbers build credibility. "Designed 50+ logos for international brands" is far more compelling than "experienced graphic designer."</p><h2>4. Showcase Your Best Work</h2><p>Upload 3–5 portfolio pieces that represent the type of work you want to attract. Quality beats quantity every time.</p><h2>5. Set Competitive Rates</h2><p>Research what top freelancers in your category charge. Starting too low devalues your skills; starting too high can deter clients.</p><h2>6. List Specific Skills</h2><p>Be specific. "JavaScript, React, REST APIs" is better than "web technologies." Clients search by specific skills.</p><h2>7. Write in First Person</h2><p>Speak directly to your client. "I help startups build scalable products" builds more connection than "Experienced developer available."</p><h2>8. Include Availability</h2><p>Let clients know your typical response time and current availability. Responsive freelancers win more projects.</p><h2>9. Get Verified</h2><p>Complete CamboFreelance''s identity and skills verification. Verified badges increase client trust significantly.</p><h2>10. Keep It Updated</h2><p>Review and refresh your profile monthly. Add new skills, update your portfolio, and adjust rates as your experience grows.</p>',
    '<p>ការចាប់ចិត្តដំបូងមានសារៈសំខាន់។ នៅក្នុងទីផ្សារការងារសេរីប្រកួតប្រជែង ប្រវត្តិរបស់អ្នកជារឿយៗជាកត្តាសម្រេចចិត្ត។</p>',
    'BLOGS',
    'CamboFreelance Editorial',
    'tips,profile,freelancing,career',
    NOW() - INTERVAL '10 days',
    1,
    'PUBLISHED', 'ACT', 'SYS'
),
(
    gen_random_uuid(),
    'How Cambodian Businesses Are Embracing the Gig Economy',
    'របៀបដែលអាជីវកម្មខ្មែរកំពុងទទួលយកសេដ្ឋកិច្ចការងារបណ្តោះអាសន្ន',
    'cambodian-businesses-embracing-gig-economy',
    'An in-depth look at how SMEs across Phnom Penh and Cambodia''s provinces are leveraging freelance talent to scale efficiently without the overhead of full-time hires.',
    'ការស្វែងយល់ស៊ីជម្រៅអំពីរបៀបដែល SME នៅទូទាំងភ្នំពេញ និងខេត្តនានាកំពុងប្រើប្រាស់អ្នកធ្វើការសេរី។',
    '<p>Cambodia''s business landscape is evolving rapidly. From garment factories in Kandal province to tech startups in Phnom Penh''s BKK1 district, businesses of all sizes are discovering the strategic advantages of freelance talent.</p><h2>The Numbers Tell the Story</h2><p>According to CamboFreelance platform data, the number of businesses posting projects grew 3x in 2025 compared to 2024. The most in-demand freelance categories are:</p><ol><li>Web & Mobile Development (34%)</li><li>Graphic Design & Branding (22%)</li><li>Content Writing & Translation (18%)</li><li>Digital Marketing & SEO (15%)</li><li>Accounting & Finance (11%)</li></ol><h2>Why Businesses Are Making the Switch</h2><p>For many Cambodian SMEs, the math is simple. Hiring a full-time senior web developer costs $800–$1,500/month in salary plus benefits. A freelancer for a specific project might cost $300–$800 — with no ongoing commitment, no benefits overhead, and instant access to specialized skills.</p><h2>Success Stories</h2><p><strong>Mekong Retail Group</strong> rebuilt their e-commerce platform using three CamboFreelance developers over 2 months, saving an estimated $8,000 compared to hiring a local agency.</p><p><strong>Angkor Organic Foods</strong> launched their rebrand using a CamboFreelance graphic designer based in Siem Reap, completing the project in 3 weeks with three rounds of revisions included.</p><h2>Challenges and How to Overcome Them</h2><p>The transition to freelance isn''t without challenges. Communication barriers, quality consistency, and project management are the three most cited concerns from Cambodian business owners. CamboFreelance addresses these with built-in project management tools, a milestone-based payment system, and a rating system that ensures accountability.</p>',
    NULL,
    'BLOGS',
    'CamboFreelance Editorial',
    'cambodia,gig-economy,business,sme,freelance',
    NOW() - INTERVAL '14 days',
    2,
    'PUBLISHED', 'ACT', 'SYS'
),

-- ── SERVICE ───────────────────────────────────────────────────────────────────
(
    gen_random_uuid(),
    'Web & Mobile Development Services',
    'សេវាកម្មអភិវឌ្ឍន៍គេហទំព័រ និងកម្មវិធីទូរស័ព្ទ',
    'web-mobile-development-services',
    'Connect with Cambodia''s top web and mobile developers. From React and Flutter to Laravel and Spring Boot — find the right developer for your project on CamboFreelance.',
    'ភ្ជាប់ជាមួយអ្នកអភិវឌ្ឍន៍គេហទំព័រ និងកម្មវិធីទូរស័ព្ទកំពូលរបស់កម្ពុជា។',
    '<h2>Web & Mobile Development on CamboFreelance</h2><p>Whether you need a landing page, a full-stack web application, or a native mobile app, CamboFreelance has vetted developers ready to bring your vision to life.</p><h2>Popular Technologies</h2><h3>Frontend</h3><ul><li>React.js / Next.js</li><li>Vue.js / Nuxt.js</li><li>Flutter (cross-platform mobile)</li><li>React Native</li></ul><h3>Backend</h3><ul><li>Spring Boot (Java)</li><li>Laravel (PHP)</li><li>Node.js / Express</li><li>Django / FastAPI (Python)</li></ul><h3>Databases</h3><ul><li>PostgreSQL, MySQL</li><li>MongoDB, Redis</li><li>Firebase</li></ul><h2>How to Hire</h2><ol><li>Post your project with requirements and budget</li><li>Receive proposals from qualified developers within 24 hours</li><li>Review portfolios and conduct interviews</li><li>Hire and manage work through our secure platform</li><li>Release payment only when satisfied with deliverables</li></ol><h2>Typical Project Rates</h2><p>Landing page: $150–$500 | E-commerce site: $800–$3,000 | Mobile app (MVP): $1,500–$5,000</p>',
    '<h2>ការអភិវឌ្ឍន៍គេហទំព័រ និងកម្មវិធីទូរស័ព្ទនៅ CamboFreelance</h2><p>មិនថាអ្នកត្រូវការទំព័រ landing ឬកម្មវិធីទូរស័ព្ទ ក៏ CamboFreelance មានអ្នកអភិវឌ្ឍន៍ដែលបានផ្ទៀងផ្ទាត់ស្រេចជួយបំពេញបំណងអ្នក។</p>',
    'SERVICE',
    'CamboFreelance Team',
    'web-development,mobile,react,flutter,service',
    NOW() - INTERVAL '20 days',
    1,
    'PUBLISHED', 'ACT', 'SYS'
),
(
    gen_random_uuid(),
    'Graphic Design & Branding Services',
    'សេវាកម្មរចនាក្រាហ្វិក និងម៉ាករបស់ក្រុមហ៊ុន',
    'graphic-design-branding-services',
    'From logo design to full brand identity systems, social media graphics to print materials — CamboFreelance designers deliver professional creative work at competitive rates.',
    'ចាប់ពីការរចនាឡូហ្គោរហូតដល់ប្រព័ន្ធអត្តសញ្ញាណម៉ាកពេញលេញ CamboFreelance ផ្តល់ស្នាដៃច្នៃប្រឌិតដ៏ជំនាញ។',
    '<h2>Creative Design Services on CamboFreelance</h2><p>Your brand''s visual identity is your most powerful marketing tool. CamboFreelance connects you with talented Cambodian designers who understand both local culture and international design standards.</p><h2>Design Services Available</h2><ul><li><strong>Logo & Brand Identity</strong> — Logo design, brand guidelines, color palettes, typography systems</li><li><strong>Social Media</strong> — Post templates, story designs, profile artwork, ad creatives</li><li><strong>Print Design</strong> — Business cards, brochures, banners, flyers, packaging</li><li><strong>UI/UX Design</strong> — App wireframes, prototypes, user flows, design systems</li><li><strong>Illustration</strong> — Custom illustrations, icons, infographics</li></ul><h2>Tools Our Designers Use</h2><p>Adobe Illustrator, Photoshop, Figma, Canva Pro, After Effects, and more.</p><h2>Typical Project Rates</h2><p>Logo design: $80–$300 | Social media package (10 posts): $50–$200 | Full brand identity: $300–$1,000 | UI/UX design (10 screens): $200–$800</p>',
    NULL,
    'SERVICE',
    'CamboFreelance Team',
    'graphic-design,branding,logo,service,creative',
    NOW() - INTERVAL '18 days',
    2,
    'PUBLISHED', 'ACT', 'SYS'
);
