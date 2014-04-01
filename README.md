dns-extract-txt
===============

extract txt records with "short" java code.

ExtractTXT takes a mode switch [ c | d | f ]
  c   collect everything and parse to StdOut
  d   collect everything and decode via Encode Class pass to StdOut
  f   same as d mode, but writes to file (5th cli-argument)
  
  
usage: ExtractTXT (c | d | f) subdomain
example uses:

ExtractTXT c subdomain your.dns.ip.address
ExtractTXT d subdomain 8.8.8.8
ExtractTXT f subdomain 8.8.8.8 outputfile.decoded.bin


Some of the DNS code is (loosely) still based on this, so following license disclaimer should be added.
Everything not under this license is released under GPL.


  Java Network Programming, Second Edition
  Merlin Hughes, Michael Shoffner, Derek Hamner
  Manning Publications Company; ISBN 188477749X
 
  http://nitric.com/jnp/
 
  Copyright (c) 1997-1999 Merlin Hughes, Michael Shoffner, Derek Hamner;
  all rights reserved.
 
  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:
 
  1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer. 
 
  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the
     distribution.
 
  THIS SOFTWARE IS PROVIDED BY THE ABOVE NAMED AUTHORS "AS IS" AND ANY
  EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
  PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHORS, THEIR
  PUBLISHER OR THEIR EMPLOYERS BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
  OF THE POSSIBILITY OF SUCH DAMAGE.
