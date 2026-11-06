# MOH 216 Clinical Requirements

Status: implementation baseline; clinical review is required before production use.

| Area | Requirement used by this implementation | Source and date | Clinical review |
|---|---|---|---|
| Maternal/child records | Preserve longitudinal, dated records with patient linkage, author, facility context, and controlled access. | Kenya MOH Mother & Child Health Handbook (2020), [official PDF](https://www.health.go.ke/sites/default/files/2023-05/Mother%20Child%20Health%20Handbook%20MOH-FOR%20PRINT.pdf); WHO *home-based records* guidance (2018), [WHO publication](https://www.who.int/publications/i/item/9789241550352). | Required |
| ANC/PNC history | Capture visit date, observations, notes, follow-up, and history without replacing clinical judgement. | Kenya MOH handbook (2020); WHO postnatal recommendations (2022), [WHO publication](https://www.who.int/publications/i/item/9789240045989). | Required |
| Immunization | Store vaccine, dose label, administration date, child, provider, and notes. No schedule or clinical threshold is hardcoded until current Kenyan guidance is verified. | Kenya MOH guidance portal, [health.go.ke](https://www.health.go.ke/); WHO vaccine position papers, [who.int](https://www.who.int/teams/immunization-vaccines-and-biologicals/policies/position-papers). | Required before schedules |
| Growth monitoring | Store dated measurements and preserve source observations; reference charts and interpretation require approved WHO/Kenya reference data. | WHO Child Growth Standards, [who.int](https://www.who.int/tools/child-growth-standards). | Required before charts/alerts |
| Reporting | Provide aggregate operational counts and date filters; exports must be authorized and must not expose unnecessary patient detail. | WHO health information guidance and applicable Kenyan data-protection obligations. | Required |

The database intentionally uses configurable labels and dates instead of inventing vaccination schedules, growth cut-offs, treatment recommendations, or alerts. Clinical governance must approve those rules and their effective dates before activation.

Research note: the current public Kenya Ministry of Health site did not expose a
reliably dated current routine-immunization schedule during this review. The
2020 handbook must therefore be version-controlled and reconciled with the
latest Kenya MoH/Kenya EPI publication before any schedule or reminder logic is
enabled.
