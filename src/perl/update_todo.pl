#!/usr/bin/perl


open (README,"README.markdown") || die "Cannot open README.markdown for reading: $!\n";

@lines = ();
while (<README>)
{
    push @lines,$_;
    last if (/## Todo/);
}

open (CL,"cl ls -u | cut  -d'	' -f3 | sed \"s/^/\* /\"|") || die "Can't run cl: $!\n";

while (<CL>)
{
    push @lines,$_;
}

close(CL);
close(README);

open (README,">README.markdown") || die "Cannot open README.markdown for writing: $!\n";
foreach (@lines) { print README; }
close (README);

system "git add README.markdown";
