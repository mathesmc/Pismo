alter table operation_types drop constraint if exists uniq_on_operation_types_description;
alter table operation_types add constraint uniq_on_operation_types_description unique(description);

alter table operation_types drop constraint if exists enum_on_operation_types_description;
alter table operation_types add constraint enum_on_operation_types_description check(description in ('COMPRA A VISTA',
                                                                                                     'COMPRA PARCELADA',
                                                                                                     'PAGAMENTO',
                                                                                                     'SAQUE'));
