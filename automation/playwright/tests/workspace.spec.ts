import { test, expect } from '@playwright/test';

test('organization admin sees only its own workspace', async ({ page }) => {
  await page.goto('/');
  await page.getByLabel('Email').fill('admin@northwind.test');
  await page.getByLabel('Password').fill('Northwind-Dev-Only!');
  await page.getByRole('button', { name: 'Continue' }).click();
  await expect(page.getByRole('heading', { name: 'Northwind Labs' })).toBeVisible();
  await expect(page.getByText('admin@contoso.test')).toHaveCount(0);
});

test('another tenant login gets a separate organization context', async ({ page }) => {
  await page.goto('/');
  await page.getByLabel('Email').fill('admin@contoso.test');
  await page.getByLabel('Password').fill('Contoso-Dev-Only!');
  await page.getByRole('button', { name: 'Continue' }).click();
  await expect(page.getByRole('heading', { name: 'Contoso Research' })).toBeVisible();
  await expect(page.getByText('admin@northwind.test')).toHaveCount(0);
});
