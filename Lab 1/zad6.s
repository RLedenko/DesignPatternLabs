.file	"zad6.cpp"
	.intel_syntax noprefix
	.text
	.section	.text$printf(char const*, ...),"x"
	.linkonce discard
	.globl	printf(char const*, ...)
	.def	printf(char const*, ...);	.scl	2;	.type	32;	.endef
	.seh_proc	printf(char const*, ...)
printf(char const*, ...):
.LFB9:
	push	rbp
	.seh_pushreg	rbp
	push	rbx
	.seh_pushreg	rbx
	sub	rsp, 56
	.seh_stackalloc	56
	lea	rbp, 48[rsp]
	.seh_setframe	rbp, 48
	.seh_endprologue
	mov	QWORD PTR 32[rbp], rcx
	mov	QWORD PTR 40[rbp], rdx
	mov	QWORD PTR 48[rbp], r8
	mov	QWORD PTR 56[rbp], r9
	lea	rax, 40[rbp]
	mov	QWORD PTR -16[rbp], rax
	mov	rbx, QWORD PTR -16[rbp]
	mov	ecx, 1
	mov	rax, QWORD PTR __imp___acrt_iob_func[rip]
	call	rax
	mov	rcx, rax
	mov	rax, QWORD PTR 32[rbp]
	mov	r8, rbx
	mov	rdx, rax
	call	__mingw_vfprintf
	mov	DWORD PTR -4[rbp], eax
	mov	eax, DWORD PTR -4[rbp]
	add	rsp, 56
	pop	rbx
	pop	rbp
	ret
	.seh_endproc
	.section	.text$Base::Base(),"x"
	.linkonce discard
	.align 2
	.globl	Base::Base()
	.def	Base::Base();	.scl	2;	.type	32;	.endef
	.seh_proc	Base::Base()
Base::Base():
.LFB46:
	push	rbp
	.seh_pushreg	rbp
	mov	rbp, rsp
	.seh_setframe	rbp, 0
	sub	rsp, 32
	.seh_stackalloc	32
	.seh_endprologue
	mov	QWORD PTR 16[rbp], rcx
	lea	rdx, vtable for Base[rip 16]
	mov	rax, QWORD PTR 16[rbp]
	mov	QWORD PTR [rax], rdx
	mov	rax, QWORD PTR 16[rbp]
	mov	rcx, rax
	call	Base::metoda()
	nop
	add	rsp, 32
	pop	rbp
	ret
	.seh_endproc
	.section .rdata,"dr"
.LC0:
	.ascii "ja sam bazna implementacija!\12\0"
	.section	.text$Base::virtualnaMetoda(),"x"
	.linkonce discard
	.align 2
	.globl	Base::virtualnaMetoda()
	.def	Base::virtualnaMetoda();	.scl	2;	.type	32;	.endef
	.seh_proc	Base::virtualnaMetoda()
Base::virtualnaMetoda():
.LFB48:
	push	rbp
	.seh_pushreg	rbp
	mov	rbp, rsp
	.seh_setframe	rbp, 0
	sub	rsp, 32
	.seh_stackalloc	32
	.seh_endprologue
	mov	QWORD PTR 16[rbp], rcx
	lea	rax, .LC0[rip]
	mov	rcx, rax
	call	printf(char const*, ...)
	nop
	add	rsp, 32
	pop	rbp
	ret
	.seh_endproc
	.section .rdata,"dr"
.LC1:
	.ascii "Metoda kaze: \0"
	.section	.text$Base::metoda(),"x"
	.linkonce discard
	.align 2
	.globl	Base::metoda()
	.def	Base::metoda();	.scl	2;	.type	32;	.endef
	.seh_proc	Base::metoda()
Base::metoda():
.LFB49:
	push	rbp
	.seh_pushreg	rbp
	mov	rbp, rsp
	.seh_setframe	rbp, 0
	sub	rsp, 32
	.seh_stackalloc	32
	.seh_endprologue
	mov	QWORD PTR 16[rbp], rcx
	lea	rax, .LC1[rip]
	mov	rcx, rax
	call	printf(char const*, ...)
	mov	rax, QWORD PTR 16[rbp]
	mov	rax, QWORD PTR [rax]
	mov	rdx, QWORD PTR [rax]
	mov	rax, QWORD PTR 16[rbp]
	mov	rcx, rax
	call	rdx
	nop
	add	rsp, 32
	pop	rbp
	ret
	.seh_endproc
	.section	.text$Derived::Derived(),"x"
	.linkonce discard
	.align 2
	.globl	Derived::Derived()
	.def	Derived::Derived();	.scl	2;	.type	32;	.endef
	.seh_proc	Derived::Derived()
Derived::Derived():
.LFB52:
	push	rbp
	.seh_pushreg	rbp
	mov	rbp, rsp
	.seh_setframe	rbp, 0
	sub	rsp, 32
	.seh_stackalloc	32
	.seh_endprologue
	mov	QWORD PTR 16[rbp], rcx
	mov	rax, QWORD PTR 16[rbp]
	mov	rcx, rax
	call	Base::Base()
	lea	rdx, vtable for Derived[rip 16]
	mov	rax, QWORD PTR 16[rbp]
	mov	QWORD PTR [rax], rdx
	mov	rax, QWORD PTR 16[rbp]
	mov	rcx, rax
	call	Base::metoda()
	nop
	add	rsp, 32
	pop	rbp
	ret
	.seh_endproc
	.section .rdata,"dr"
	.align 8
.LC2:
	.ascii "ja sam izvedena implementacija!\12\0"
	.section	.text$Derived::virtualnaMetoda(),"x"
	.linkonce discard
	.align 2
	.globl	Derived::virtualnaMetoda()
	.def	Derived::virtualnaMetoda();	.scl	2;	.type	32;	.endef
	.seh_proc	Derived::virtualnaMetoda()
Derived::virtualnaMetoda():
.LFB53:
	push	rbp
	.seh_pushreg	rbp
	mov	rbp, rsp
	.seh_setframe	rbp, 0
	sub	rsp, 32
	.seh_stackalloc	32
	.seh_endprologue
	mov	QWORD PTR 16[rbp], rcx
	lea	rax, .LC2[rip]
	mov	rcx, rax
	call	printf(char const*, ...)
	nop
	add	rsp, 32
	pop	rbp
	ret
	.seh_endproc
	.def	__main;	.scl	2;	.type	32;	.endef
	.text
	.globl	main
	.def	main;	.scl	2;	.type	32;	.endef
	.seh_proc	main
main:
.LFB54:
	push	rbp
	.seh_pushreg	rbp
	push	rdi
	.seh_pushreg	rdi
	push	rsi
	.seh_pushreg	rsi
	push	rbx
	.seh_pushreg	rbx
	sub	rsp, 56
	.seh_stackalloc	56
	lea	rbp, 48[rsp]
	.seh_setframe	rbp, 48
	.seh_endprologue
	call	__main
	mov	ecx, 8
.LEHB0:
	call	operator new(unsigned long long)
.LEHE0:
	mov	rbx, rax
	mov	edi, 1
	mov	rcx, rbx
.LEHB1:
	call	Derived::Derived()
.LEHE1:
	mov	QWORD PTR -8[rbp], rbx
	mov	rax, QWORD PTR -8[rbp]
	mov	rcx, rax
.LEHB2:
	call	Base::metoda()
	mov	eax, 0
	jmp	.L13
.L12:
	mov	rsi, rax
	test	dil, dil
	je	.L11
	mov	edx, 8
	mov	rcx, rbx
	call	operator delete(void*, unsigned long long)
.L11:
	mov	rax, rsi
	mov	rcx, rax
	call	_Unwind_Resume
.LEHE2:
.L13:
	add	rsp, 56
	pop	rbx
	pop	rsi
	pop	rdi
	pop	rbp
	ret
	.def	__gxx_personality_seh0;	.scl	2;	.type	32;	.endef
	.seh_handler	__gxx_personality_seh0, @unwind, @except
	.seh_handlerdata
.LLSDA54:
	.byte	0xff
	.byte	0xff
	.byte	0x1
	.uleb128 .LLSDACSE54-.LLSDACSB54
.LLSDACSB54:
	.uleb128 .LEHB0-.LFB54
	.uleb128 .LEHE0-.LEHB0
	.uleb128 0
	.uleb128 0
	.uleb128 .LEHB1-.LFB54
	.uleb128 .LEHE1-.LEHB1
	.uleb128 .L12-.LFB54
	.uleb128 0
	.uleb128 .LEHB2-.LFB54
	.uleb128 .LEHE2-.LEHB2
	.uleb128 0
	.uleb128 0
.LLSDACSE54:
	.text
	.seh_endproc
	.globl	vtable for Derived
	.section	.rdata$vtable for Derived,"dr"
	.linkonce same_size
	.align 8
vtable for Derived:
	.quad	0
	.quad	typeinfo for Derived
	.quad	Derived::virtualnaMetoda()
	.globl	vtable for Base
	.section	.rdata$vtable for Base,"dr"
	.linkonce same_size
	.align 8
vtable for Base:
	.quad	0
	.quad	typeinfo for Base
	.quad	Base::virtualnaMetoda()
	.globl	typeinfo for Derived
	.section	.rdata$typeinfo for Derived,"dr"
	.linkonce same_size
	.align 8
typeinfo for Derived:
	.quad	vtable for __cxxabiv1::__si_class_type_info 16
	.quad	typeinfo name for Derived
	.quad	typeinfo for Base
	.globl	typeinfo name for Derived
	.section	.rdata$typeinfo name for Derived,"dr"
	.linkonce same_size
	.align 8
typeinfo name for Derived:
	.ascii "7Derived\0"
	.globl	typeinfo for Base
	.section	.rdata$typeinfo for Base,"dr"
	.linkonce same_size
	.align 8
typeinfo for Base:
	.quad	vtable for __cxxabiv1::__class_type_info 16
	.quad	typeinfo name for Base
	.globl	typeinfo name for Base
	.section	.rdata$typeinfo name for Base,"dr"
	.linkonce same_size
typeinfo name for Base:
	.ascii "4Base\0"
	.ident	"GCC: (MinGW-W64 x86_64-msvcrt-posix-seh, built by Brecht Sanders) 13.2.0"
	.def	__mingw_vfprintf;	.scl	2;	.type	32;	.endef
	.def	operator new(unsigned long long);	.scl	2;	.type	32;	.endef
	.def	operator delete(void*, unsigned long long);	.scl	2;	.type	32;	.endef
	.def	_Unwind_Resume;	.scl	2;	.type	32;	.endef