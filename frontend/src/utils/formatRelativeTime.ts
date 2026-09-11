
export function formatRelativeTime(dateString: string): string {
  const date = new Date(dateString);
  
  // guard clause for invalid dates
  if (isNaN(date.getTime())) return '---';

  // Math.max maks sure diffMs is never negative due to server/device clock mismatch
  const diffMs = Math.max(0, Date.now() - date.getTime());
  const diffMinutes = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMinutes / 60);
  const diffDays = Math.floor(diffHours / 24);

  if (diffMinutes < 1) return 'Just now';
  if (diffMinutes < 60) return `${diffMinutes}m ago`;
  if (diffHours < 24) return `${diffHours}h ago`;
  if (diffDays < 7) return `${diffDays}d ago`;

  return date.toLocaleDateString();
}